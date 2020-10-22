package server

import http.HelloRoutes
import org.http4s.server.blaze._
import zio.{ ExitCode => ZExitCode, _ }
import zio.interop.catz._
import zio.interop.catz.implicits._

import scala.concurrent.ExecutionContext.global

object Server extends zio.App {

  override def run(args: List[String]): URIO[ZEnv, ZExitCode] = {
    val server: ZIO[ZEnv, Throwable, Unit] = ZIO.runtime[ZEnv].flatMap { implicit rts =>
      BlazeServerBuilder[Task](global).withHttpApp(HelloRoutes.routes).serve.compile.drain
    }
    server.fold(_ => ZExitCode.failure, _ => ZExitCode.success)
  }
}
