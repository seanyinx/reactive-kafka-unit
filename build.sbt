name := "reactive-kafka-unit"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4.4",
  "com.typesafe.akka" %% "akka-cluster" % "2.4.4",
  "com.typesafe.akka" %% "akka-remote" % "2.4.4",
  "com.typesafe.akka" %% "akka-slf4j" % "2.4.4",
  "com.typesafe.akka" %% "akka-stream" % "2.4.4",
  "com.typesafe.akka" %% "akka-persistence" % "2.4.4",
  "com.typesafe.akka" %% "akka-http-experimental" % "2.4.4",
  "com.typesafe.akka" %% "akka-http-spray-json-experimental" % "2.4.4",
  "com.typesafe.akka" %% "akka-testkit" % "2.4.4" % "test",
  "com.typesafe.akka" %% "akka-http-testkit" % "2.4.4" % "test"
)
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream-kafka" % "0.11-M2"
)

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "ch.qos.logback" % "logback-core" % "1.1.3",
  "org.slf4j" % "jcl-over-slf4j" % "1.7.12"
)

libraryDependencies ++= Seq(
  "org.scalactic" %% "scalactic" % "2.2.6",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "net.manub" %% "scalatest-embedded-kafka" % "0.6.1" % "test",
  "com.github.charithe" % "kafka-junit" % "2.1" % "test"
)
