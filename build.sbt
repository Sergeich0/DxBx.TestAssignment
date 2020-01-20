name := """Play28ta3"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.12.9"

libraryDependencies ++= Seq(
  guice,
  jdbc,
  javaJdbc,
  "javax.persistence" % "persistence-api" % "1.0.2",
  javaWs,
  "io.ebean" % "ebean" % "11.22.10",
  "org.postgresql" % "postgresql" % "42.2.5",
  "com.sun.xml.bind" % "jaxb-impl" % "2.3.1",
  "javax.xml.bind" % "jaxb-api" % "2.2.12",
  "com.sun.istack" % "istack-commons-tools" % "3.0.9",
  "com.sun.istack" % "istack-commons-runtime" % "3.0.10"
)
