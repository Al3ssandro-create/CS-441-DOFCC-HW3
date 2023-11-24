
ThisBuild / version := "0.1.0-SNAPSHOT"
Global / excludeLintKeys +=   test / fork
Global / excludeLintKeys += run / mainClass
ThisBuild / scalaVersion := "2.13.12"

lazy val root = (project in file("."))
  .settings(
    name := "CS-441-DOFCC-HW3"
  )
resolvers ++= Seq(
  "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"
)
compileOrder := CompileOrder.JavaThenScala
test / fork := true
run / fork := true
run / javaOptions ++= Seq(
  "-Xms2G",
  "-Xmx8G",
  "-XX:+UseG1GC"
)
Compile / mainClass := Some("Server.WebServer")
run / mainClass := Some("Server.WebServer")
assembly / mainClass := Some("Server.WebServer")
val jarName = "PolicevsThief.jar"
assembly/assemblyJarName := jarName
ThisBuild / assemblyMergeStrategy := {
  case PathList("META-INF", _*) => MergeStrategy.discard
  case "application.conf" => MergeStrategy.concat
  case _ => MergeStrategy.first
}
libraryDependencies ++= Seq(
  // Akka HTTP for RESTful service
  "com.typesafe.akka" %% "akka-http" % "10.5.0", // replace with the latest version
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.5.0", // for JSON marshalling/unmarshalling
  "com.typesafe.akka" %% "akka-http" % "10.5.0", // use the latest version
  "com.typesafe.akka" %% "akka-actor-typed" % "2.8.0",
  // Akka HTTP TestKit for testing
  "com.typesafe" % "config" % "1.4.2",
  // Akka Streams
  "com.typesafe.akka" %% "akka-stream" % "2.8.0", // replace with the latest version

  // Akka Actor
  "com.typesafe.akka" %% "akka-actor-typed" % "2.8.0", // replace with the latest version
  // Log4j dependencies
  "org.slf4j" % "slf4j-api" % "2.0.5",
  "org.slf4j" % "slf4j-log4j12" % "2.0.5",
  "log4j" % "log4j" % "1.2.17",

  // Scala Test for testing (optional but recommended)
  "org.scalatest" %% "scalatest" % "3.2.15" % Test, // replace with the latest version
  // JSON library
  "org.json4s" %% "json4s-native" % "4.0.6",
  // Guava
  "com.google.guava" % "guava" % "31.0.1-jre",
  // Scala Java HTTP
  "org.scalaj" %% "scalaj-http" % "2.4.2"
)