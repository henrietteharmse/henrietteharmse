ThisBuild / scalaVersion := "2.13.2"
ThisBuild / organization := "org.henrietteharmse.tutorial"

val setLog4jDebug = sys.props("log4j2.debug") = "true"

lazy val root = (project in file("."))
  .settings(
    name := "scala-logging-with-log4j2",
	  libraryDependencies ++= Seq(
	    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
        "org.slf4j" % "slf4j-api" % "1.7.30",
        "org.apache.logging.log4j" % "log4j-slf4j-impl" % "2.13.3"
	    ),
    scalacOptions ++= Seq("-deprecation")
)

