package server

import cats.effect._
import fs2.Stream.Compiler._
import org.http4s.HttpApp
import org.http4s.implicits._
import org.http4s.server.blaze._
import zio.clock.Clock
import zio.{ ExitCode => ZExitCode, _ }
import zio.interop.catz._
import config._
import http._
import org.http4s.server.Router

object Server extends App {

  override def run(args: List[String]): URIO[zio.ZEnv, ZExitCode] = {
    val program = for {
      cfg    <- getAppConfig
      _      <- logging.log.info(s"starting with $cfg")
      httpApp = Router[AppTask](routeMappings.toSeq: _*).orNotFound
      _      <- runHttp(httpApp, cfg.http.port, cfg.http.host)
    } yield ZExitCode.success

    program.provideSomeLayer[ZEnv](Layers.live.appLayer).orDie
  }

  def runHttp[R <: Clock](httpApp: HttpApp[RIO[R, *]], port: Int, host: String): ZIO[R, Throwable, Unit] = {
    type Task[A] = RIO[R, A]

    ZIO.runtime[R].flatMap { implicit runtime =>
      BlazeServerBuilder[Task](runtime.platform.executor.asEC)
        .bindHttp(port, host)
        .withHttpApp(httpApp)
        .serve
        .compile[Task, Task, ExitCode]
        .drain
    }
  }
}
