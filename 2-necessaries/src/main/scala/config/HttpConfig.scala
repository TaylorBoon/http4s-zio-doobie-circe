package config

import pureconfig._
import pureconfig.generic.semiauto._
import zio._

object HttpConfig {
  final case class Config(host: String, port: Int)

  object Config {
    implicit val convert: ConfigConvert[Config] = deriveConvert
  }

  val fromAppConfig: ZLayer[AppConfig, Nothing, HttpConfig] = ZLayer.fromService(_.http)
}
