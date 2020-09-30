package org.learning.reactive.extensions.combining.observables

import org.learning.reactive.extensions.core.{Observable, log, sleep}

import scala.concurrent.duration._

object Example11 extends App {

  val src1 = Observable.just("Alpha", "Beta")
  val src2 = Observable.just("Zeta", "Eta")

  Observable.concat(src1, src2)
    .subscribe(str => log(s"Received: $str"))

}

object Example12 extends App {

  val src1 = Observable.just("Alpha", "Beta")
  val src2 = Observable.just("Zeta", "Eta")

  src1.concatWith(src2)
    .subscribe(str => log(s"Received: $str"))

}

object Example13 extends App {

  Observable.concat(src1, src2)
    .subscribe(str => log(s"Received: $str"))

  sleep(7.seconds)

  def src1: Observable[String] =
    Observable.interval(1.seconds)
      .take(2)
      .map(_ + 1)
      .map(n => s"Source1: $n seconds")

  def src2: Observable[String] =
    Observable.interval(300.milliseconds)
      .map(n => (n + 1) * 300)
      .map(n => s"Source2: $n milliseconds")

}

object Example14 extends App {

  Observable.just("Alpha", "Beta", "Gamma")
    .concatMap(str => Observable.from(str.split("").toSeq))
    .subscribe(str => log(s"Received: $str"))

}
