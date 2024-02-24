ThisBuild / version := "1.0.0"

scalaVersion := "2.12.18"

libraryDependencies += "org.apache.spark" %% "spark-sql" % "3.5.0" % "provided"
libraryDependencies += "io.delta" %% "delta-spark" % "3.1.0"
libraryDependencies += "org.apache.spark" %% "spark-sql-kafka-0-10" % "3.5.0"
libraryDependencies += "org.apache.hadoop" % "hadoop-aws" % "3.3.4"


lazy val root = (project in file("."))
  .settings(
    name := "spark-structured-streaming"
  )
