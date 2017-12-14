name := "weatherdata"

version := "0.1"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.scalaj" %% "scalaj-http" % "2.3.0",
  "net.liftweb" %% "lift-json" % "3.1.1",
  "com.typesafe.akka" %% "akka-http" % "10.0.11",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.11",
  "com.typesafe.akka" %% "akka-stream" % "2.5.7",
  "com.typesafe.akka" %% "akka-actor"  % "2.5.7"
)


