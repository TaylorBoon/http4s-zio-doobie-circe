import sbt._

object Dependencies {
  val http4sVersion = "0.21.7"
  val zioVersion = "1.0.3"

  val scalaTest = "org.scalatest" %% "scalatest" % "3.2.2"


  val http4sBlazeClient = "org.http4s" %% "http4s-blaze-client" % http4sVersion
  val http4sBlazeServer = "org.http4s" %% "http4s-blaze-server" % http4sVersion
  val http4sDsl =         "org.http4s" %% "http4s-dsl"          % http4sVersion
  val http4sCirce =       "org.http4s" %% "http4s-circe"        % http4sVersion

  val circe =             "io.circe"   %% "circe-generic"       % "0.13.0"

  val zio =               "dev.zio"    %% "zio"                 % zioVersion
  val zioCats =           "dev.zio"    %% "zio-interop-cats"    % "2.2.0.1"

  val betterMonadicFor   = "com.olegpy"            %% "better-monadic-for"   % "0.3.1"
  val kindProjector      = "org.typelevel"         % "kind-projector_2.13.3" % "0.11.0"
}
