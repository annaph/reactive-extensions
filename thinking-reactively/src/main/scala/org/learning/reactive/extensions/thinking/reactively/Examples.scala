package org.learning.reactive.extensions.thinking.reactively

import org.learning.reactive.extensions.core.{Observable, log, sleep}

import scala.concurrent.duration._

object Example1 extends App {

  val myStrings = Observable.just("Alpha", "Beta", "Gamma")
  myStrings subscribe log _

}

object Example2 extends App {

  val myStrings = Observable.just("Alpha", "Beta", "Gamma")
  myStrings.map(_.length).subscribe(length => log(s"$length"))

}

object Example3 extends App {

  val secondIntervals = Observable.interval(1.seconds)
  secondIntervals.subscribe(n => log(s"$n"))

  sleep(7.seconds)

}
