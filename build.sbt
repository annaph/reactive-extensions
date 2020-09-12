ThisBuild / organization := "org.learning.reactive.extensions"

ThisBuild / version := "1.0.0"

ThisBuild / scalaVersion := "2.13.2"

ThisBuild / scalacOptions ++= Seq(
  "-encoding", "utf8",
  "-deprecation",
  "-feature",
  "-unchecked",
  "-language:implicitConversions")

ThisBuild / libraryDependencies ++= Seq(
    "io.reactivex.rxjava3" % "rxjava" % "3.0.4")

ThisBuild / fork := true
  
lazy val root = Project("reactive-extensions", file("."))
  .aggregate(core, thinkingReactively, observableAndObserver, basicOperators)

lazy val core = (project in file("core"))

lazy val thinkingReactively = Project("thinking-reactively", file("thinking-reactively"))
	.dependsOn(core)

lazy val observableAndObserver =  Project("observable-and-observer", file("observable-and-observer"))
  .dependsOn(core)

lazy val basicOperators =  Project("basic-operators", file("basic-operators"))
  .dependsOn(core)
