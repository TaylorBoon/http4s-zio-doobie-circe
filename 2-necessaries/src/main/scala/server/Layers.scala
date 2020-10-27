package server

import config._
import zio._
import zio.blocking.Blocking
import zio.logging.Logging
import zio.logging.slf4j.Slf4jLogger

object Layers {
  type Layer0Env = AppConfig with Logging with Blocking

  type AppEnv = Layer0Env

  object live {

    val layer0: ZLayer[Blocking, Throwable, Layer0Env] =
      Blocking.any ++ AppConfig.live ++ Slf4jLogger.make((_, msg) => msg)

    val appLayer: ZLayer[Blocking, Throwable, AppEnv] = layer0
  }
}
