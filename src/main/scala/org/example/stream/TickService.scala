package org.example.stream

import akka.http.scaladsl.common.{EntityStreamingSupport, JsonEntityStreamingSupport}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.stream.ThrottleMode
import akka.stream.scaladsl.{Flow, Source}
import spray.json._

import scala.concurrent.duration._

case class Tick(tick:Int)

trait Protocols extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val tickFormat: RootJsonFormat[Tick] = jsonFormat1(Tick.apply)
}

class TickService extends Protocols{

  implicit val jsonStreamingSupport: JsonEntityStreamingSupport = EntityStreamingSupport.json().withParallelMarshalling(parallelism = 2, unordered = true)

  //Source that generates Tick with incremental numbers
  def incrementalSource = Source.fromIterator(() => Iterator.iterate[Tick](Tick(1))(t=>Tick(t.tick+1)))

  //Source that generates Tick with prime numbers. If number isn't prime -1 value is produced
  def primeSource = {
    //Credits: https://stackoverflow.com/questions/36882103/checking-whether-a-number-is-prime-in-scala
    def isPrime(n: Int): Boolean = ! ((2 until n-1) exists (n % _ == 0))
    def nextEvenTick(state: Int) = if(isPrime(state)) Some(state+1, Tick(state)) else Some(state+1, Tick(-1))
    Source.unfold(1)(nextEvenTick)
  }

  def getThrottlingFlow[T] = Flow[T].throttle(elements = 1, per = 10.millis, maximumBurst = 0, mode = ThrottleMode.shaping)

  val route =
    pathPrefix("tickStream") {
      pathEnd {
        (get) (complete(incrementalSource.via(getThrottlingFlow[Tick])))
      }
    } ~
      pathPrefix("primeTickStream") {
        pathEnd {
          (get) (complete(primeSource.via(getThrottlingFlow[Tick])))
        }
      }
}