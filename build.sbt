import org.eclipse.aether.version.VersionScheme

name := """BidMaster"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)


scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  evolutions
)
libraryDependencies ++= Seq(
  "com.mysql" % "mysql-connector-j" % "8.0.33",
  "org.mockito" % "mockito-core" % "2.8.9",
  "junit" % "junit" % "4.10"
)
fork in run := true
fork in Test := true
javaOptions in Test += "-Dconfig.file=conf/application-test.conf"

import de.johoop.jacoco4sbt.JacocoPlugin.jacoco

// Apply the Jacoco settings to the project
Seq(jacoco.settings: _*)

jacoco.excludes in jacoco.Config := Seq(
  "views.*"
)

javaOptions in Test += "-Xmx2G" // Adjust based on your system's memory
parallelExecution in Test := false