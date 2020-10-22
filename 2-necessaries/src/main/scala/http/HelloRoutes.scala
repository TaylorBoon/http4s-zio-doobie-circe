package http

import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits._
import zio.Task
import zio.interop.catz._

object HelloRoutes {
  private val dsl = Http4sDsl[Task]
  import dsl._

  val routes = HttpRoutes.of[Task] { case GET -> Root / "hello" / name => Ok(s"Hello, $name.") }.orNotFound

}
