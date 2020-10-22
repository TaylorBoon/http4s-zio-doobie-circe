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
We start by defining a Server, derived from the trait `zio.App`. `zio.App` provides a `main` function which will be the entry point to our application. We must provide a `run` function that describes the computation to be run (in this case handle HTTP requests). `zio.App` is analogous to the cats-effect trait `IOApp`, although of course the internals are somewhat different.

Inside `run`, we begin by describing our server. Let's break down each step and see what they do:

`ZIO.runtime[ZEnv].flatmap { implicit rts =>`

`ZIO.runtime` creates a `Runtime` which is a combination of an `Environment` and a `Platform`. A `Runtime`



 

#### Routes
Similarly to Server, the entirety of this object is below. We want to understand at some level what is going on here. This is foundational and a good sense of what is happening is important as we get to more complex functionality.  
```scala
object HelloRoutes {
  private val dsl = Http4sDsl[Task]
  import dsl._

  val routes = HttpRoutes.of[Task] { case GET -> Root / "hello" / name => Ok(s"Hello, $name.") }.orNotFound

}
```
## Other Notes
- Circe has been added as a dependency although it is not used in this version of the project. It was added to bring in all 
the http4s dependencies, namely `http4s-circe`, at once, which requires the `circe` library to be included as well.   
 