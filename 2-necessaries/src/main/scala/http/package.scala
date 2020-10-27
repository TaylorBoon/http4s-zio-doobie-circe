import org.http4s.HttpRoutes
import server.Layers
import zio.RIO
import zio.clock.Clock

package object http {
  type AppTask[A] = RIO[Layers.AppEnv with Clock, A]

  val routeMappings: Map[String, HttpRoutes[AppTask]] = Map(
    "hello" -> HelloRoutes.routes()
  )
}
