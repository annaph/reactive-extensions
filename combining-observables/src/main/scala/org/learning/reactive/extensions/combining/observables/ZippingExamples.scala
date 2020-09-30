package org.learning.reactive.extensions.combining.observables

import java.time.LocalDateTime

import org.learning.reactive.extensions.core.{Observable, log, sleep}

import scala.concurrent.duration._

object Example17 extends App {

  val src1 = Observable.just("Alpha", "Beta", "Gamma")
  val src2 = Observable.range(1, 6)

  Observable.zip(src1, src2)((str, n) => s"$str-$n")
    .subscribe(str => log(s"Received: $str"))

}

object Example18 extends App {

  val strings = Observable.just("Alpha", "Beta", "Gamma")

  val seconds = Observable.interval(1.seconds)

  Observable.zip(strings, seconds)((str, _) => str)
    .subscribe(str => log(s"Received: $str at ${LocalDateTime.now()}"))

  sleep(7.seconds)

}
