name := "Mulligan"

version := "0.1"

scalaVersion := "2.13.8"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.9"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.9" % "test"

// API stuff
val http4sVersion = "1.0.0-M35"
val circeVersion = "0.14.2"

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-ember-server" % http4sVersion,
  "org.http4s" %% "http4s-circe" % http4sVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-literal" % circeVersion,
  "org.typelevel" %% "cats-effect" % "3.3.12",
  "org.http4s" %% "http4s-circe" % http4sVersion,
  "org.scalatra.scalate" %% "scalate-core" % "1.9.6"
)

publishTo := Some(Resolver.file("file",  new File( "./artifacts/" )) )