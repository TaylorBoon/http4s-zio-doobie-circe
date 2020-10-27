import Dependencies._

lazy val commonSettings = Seq(
  scalaVersion := "2.13.3",
  version := "0.1.0-SNAPSHOT",
  organization := "com.alltrust",
  scalafmtOnCompile := true,
  resolvers += Resolver.sonatypeRepo("snapshots"),

  libraryDependencies ++= Seq(
    compilerPlugin(kindProjector),
    compilerPlugin(betterMonadicFor),

    http4sBlazeClient,
    http4sBlazeServer,
    http4sDsl,
    http4sCirce,
    circe,
    zio,
    zioCats,
    zioLogging,
    zioLoggingSlf4j,
    logbackClassic,
    pureconfig
  ),
  scalacOptions += "-Ymacro-annotations"//,
  //Compile / compile / wartremoverWarnings ++= Warts.unsafe
)

lazy val root = (project in file("."))
  .settings(
    name := "2-necessaries",
    commonSettings,
    libraryDependencies += scalaTest % Test
  )
