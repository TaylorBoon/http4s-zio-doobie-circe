package http

import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import zio._
import zio.interop.catz._

object HelloRoutes {

  def routes[R](): HttpRoutes[RIO[R, *]] = {
    type HelloTask[A] = RIO[R, A]
    val dsl: Http4sDsl[HelloTask] = Http4sDsl[HelloTask]
    import dsl._

    HttpRoutes.of[HelloTask] { case GET -> Root / name => Ok(s"Hello, $name.") }
  }

}
