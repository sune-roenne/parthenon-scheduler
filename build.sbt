name := "scheduler"

organization := "parthenon"

version := "1.1-SNAPSHOT"

scalaVersion := "2.11.2"

libraryDependencies ++= Seq(
   "com.typesafe"%"config"%"1.2.1",
   "commons-io" % "commons-io" % "2.4",
   "org.slf4j" % "slf4j-api" % "1.7.7",
   "com.h2database" % "h2" % "1.4.181",
   "com.jolbox" % "bonecp" % "0.8.0.RELEASE",
   "com.typesafe.slick" %% "slick" % "2.1.0"
   ) 

EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource

//retrieveManaged := true
