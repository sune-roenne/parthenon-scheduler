name := "scheduler"

organization := "parthenon"

version := "1.1-SNAPSHOT"

scalaVersion := "2.11.2"

libraryDependencies ++= Seq(
   "com.typesafe"%"config"%"1.2.1",
   "org.apache.commons" % "commons-lang3" % "3.3.2",   
   "commons-io" % "commons-io" % "2.4",
   "org.slf4j" % "slf4j-api" % "1.7.7",
   "com.h2database" % "h2" % "1.4.181",
   "com.jolbox" % "bonecp" % "0.8.0.RELEASE",
   "com.typesafe.slick" %% "slick" % "2.1.0",
   "org.scala-lang" % "scala-compiler" % scalaVersion.value,
   "org.scala-lang.modules" %% "scala-xml" % "1.0.2"   
   ) 

EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource

//retrieveManaged := true
