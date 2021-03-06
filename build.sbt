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
  "io.reactivex.rxjava3" % "rxjava" % "3.0.4",
  "junit" % "junit" % "4.13.2" % Test,
  "com.novocode" % "junit-interface" % "0.11" % Test)

ThisBuild / fork := true

lazy val root = Project("reactive-extensions", file("."))
  .aggregate(
    core,
    thinkingReactively,
    observableAndObserver,
    basicOperators,
    combiningObservables,
    multicasting,
    concurrency,
    buffering,
    backpressure,
    customOperators,
    testing
  )

lazy val core = project in file("core")

lazy val thinkingReactively = Project("thinking-reactively", file("thinking-reactively"))
	.dependsOn(core)

lazy val observableAndObserver =  Project("observable-and-observer", file("observable-and-observer"))
  .dependsOn(core)

lazy val basicOperators =  Project("basic-operators", file("basic-operators"))
  .dependsOn(core)

lazy val combiningObservables =  Project("combining-observables", file("combining-observables"))
  .dependsOn(core)

lazy val multicasting =  project.in(file("multicasting"))
  .dependsOn(core)

lazy val concurrency =  project.in(file("concurrency"))
  .dependsOn(core)

lazy val buffering =  project.in(file("buffering"))
  .dependsOn(core)

lazy val backpressure =  project.in(file("backpressure"))
  .dependsOn(core)

lazy val customOperators =  Project("custom-operators", file("custom-operators"))
  .dependsOn(core)

lazy val testing =  project.in(file("testing"))
  .dependsOn(core)

testOptions += Tests.Argument(TestFrameworks.JUnit, args = "-q", "-v")
