# Set Up

This project sets up the most basic http4s + ZIO application.

## Dependencies
Dependencies are defined in `project/Dependencies.scala`. The main dependencies used in this version of the project are detailed below. 

### http4s
[http4s](https://http4s.org/) provides the HTTP component of the project. 

### Cats
[Cats](https://typelevel.org/cats/) is a required dependency of http4s.

### ZIO
[ZIO](https://zio.dev/) is used for providing effects in lieu of [Cats-Effect](https://typelevel.org/cats-effect/). Learning ZIO
in a "real-world" context is the primary purpose of this project.

## Code
A brief description of some of the code is given below. This is not intended as a replacement for the documentation of the various libraries used. It is more to help understand how the libraries are used and what is actually going on. So, some of this will not make any sense unless you read (some of) the various documentation for http4s and ZIO.
#### Server
Since the code in this version is so small, here it is in its entirety. I want to describe what is going on, as best I can, because everything builds on top of this.
```scala
object Server extends zio.App {

  override def run(args: List[String]): URIO[ZEnv, ZExitCode] = {
    val server: ZIO[ZEnv, Throwable, Unit] = ZIO.runtime[ZEnv].flatMap { implicit rts =>
      BlazeServerBuilder[Task](global).withHttpApp(HelloRoutes.routes).serve.compile.drain
    }
    server.fold(_ => ZExitCode.failure, _ => ZExitCode.success)
  }
}
```

***This is a mess, I have no idea how this works except that it does***

#### Routes
As with Server, the entirety of the routes object is below. We want to understand at some level what is going on here. This is foundational and a good sense of what is happening is important as we get to more complex functionality.  
```scala
object HelloRoutes {
  private val dsl = Http4sDsl[Task]
  import dsl._

  val routes = HttpRoutes.of[Task] { case GET -> Root / "hello" / name => Ok(s"Hello, $name.") }.orNotFound

}
```

***This is also a mess, I have no idea how this works***

## Other Notes
- Circe has been added as a dependency although it is not used in this version of the project. It was added to bring in all 
the http4s dependencies, namely `http4s-circe`, at once, which requires the `circe` library to be included as well.   

## Running
We will use [curl](https://curl.haxx.se/) to exercise our code.

First, let's run from inside sbt:
```
sbt:1-setup> run
[info] running server.Server
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
```

In another console, let's make a request using curl:
```
> curl -sD - http://localhost:8080/hello/world
HTTP/1.1 200 OK
Content-Type: text/plain; charset=UTF-8
Date: Thu, 22 Oct 2020 21:32:21 GMT
Content-Length: 13

Hello, world.
```

We are running curl with the command line option to view response headers as well as actual content (the `-D -` option; `-s` suppresses progress meter).

We can also test with an invalid request:
```
> curl -sD - http://localhost:8080/invalid
HTTP/1.1 404 Not Found
Content-Type: text/plain; charset=UTF-8
Date: Thu, 22 Oct 2020 21:35:47 GMT
Content-Length: 9

Not found
```

which also responds as expected.