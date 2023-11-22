ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.12"

lazy val root = (project in file("."))
  .settings(
    name := "CS-441-DOFCC-HW3"
  )
resolvers ++= Seq(
  "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"
)

libraryDependencies ++= Seq(
  // Akka HTTP for RESTful service
  "com.typesafe.akka" %% "akka-http" % "10.5.0", // replace with the latest version
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.5.0", // for JSON marshalling/unmarshalling
  "com.typesafe.akka" %% "akka-http" % "10.5.0", // use the latest version
  "com.typesafe.akka" %% "akka-actor-typed" % "2.8.0",
  //
  "com.typesafe" % "config" % "1.4.2",
  // Akka Streams
  "com.typesafe.akka" %% "akka-stream" % "2.8.0", // replace with the latest version

  // Akka Actor
  "com.typesafe.akka" %% "akka-actor-typed" % "2.8.0", // replace with the latest version

  // SLF4J for logging (optional but recommended)
  "com.typesafe.akka" %% "akka-slf4j" % "2.8.0", // replace with the latest version
  // Log4j dependencies
  "org.slf4j" % "slf4j-api" % "1.7.30",
  "org.slf4j" % "slf4j-log4j12" % "1.7.30",
  "log4j" % "log4j" % "1.2.17",

  // Scala Test for testing (optional but recommended)
  "org.scalatest" %% "scalatest" % "3.2.15" % Test, // replace with the latest version
  // JSON library
  "org.json4s" %% "json4s-native" % "4.0.6"
)