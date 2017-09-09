Example of streaming JSON objects using [akka http](http://akka.io) on the backend and [oboe.js](http://oboejs.com) on the frontend to handle events.

## Run

```
sbt run
```

## Test
1) Open `localhost:9000` whilst the server is running to view stream events.
2) curl -m 1 http://localhost:9000/tickStream - Stream that generates Tick with incremental numbers
3) curl -m 1 http://localhost:9000/primeTickStream - Stream that generates Tick with prime numbers

![Execution example](https://raw.github.com/olka/akka-http-streaming-oboe-example/master/GX38z5N9D0.gif)


Inspired by https://github.com/benblack86/spray-oboe-json-streaming by [Benjamin Black](https://github.com/benblack86)