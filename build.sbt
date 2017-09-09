name := "akka-http-streaming-oboe-example"
version := "1.0"
scalaVersion := "2.12.2"

libraryDependencies ++= {
  val akkaV       = "2.5.4"
  val akkaHttpV   = "10.0.10"

  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-stream" % akkaV,
    "com.typesafe.akka" %% "akka-http" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpV
  )
}