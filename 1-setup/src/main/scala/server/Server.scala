package server

import cats.effect.ExitCode
import fs2.Stream
import http.HelloRoutes
import org.http4s.server.blaze._
import zio.{ ExitCode => ZExitCode, _ }
import zio.interop.catz._
import zio.interop.catz.implicits._

import scala.concurrent.ExecutionContext.global

object Server extends App {

  override def run(args: List[String]): URIO[ZEnv, ZExitCode] = {
    val server: ZIO[ZEnv, Throwable, Unit]               = ZIO.runtime[ZEnv].flatMap { implicit runtime: Runtime[ZEnv] =>
      val serverBuilder1: BlazeServerBuilder[Task]                  = BlazeServerBuilder[Task](global)
      val serverBuilder2: BlazeServerBuilder[Task]                  = serverBuilder1.withHttpApp(HelloRoutes.routes)
      val fs2Stream: fs2.Stream[Task, ExitCode]                     = serverBuilder2.serve
      val streamProjection: Stream.CompileOps[Task, Task, ExitCode] = fs2Stream.compile
      val task: Task[Unit]                                          = streamProjection.drain
      task
    }
    val infallibleWithEnv: ZIO[ZEnv, Nothing, ZExitCode] = server.fold(_ => ZExitCode.failure, _ => ZExitCode.success)
    infallibleWithEnv
  }
}
