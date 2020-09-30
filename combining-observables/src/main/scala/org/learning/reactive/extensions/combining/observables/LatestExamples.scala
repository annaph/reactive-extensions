package org.learning.reactive.extensions.combining.observables

import org.learning.reactive.extensions.core.{Observable, log, sleep}

import scala.concurrent.duration._

object Example19 extends App {

  Observable.combineLatest(src1, src2)((n1, n2) => s"Source1: $n1 Source2: $n2")
    .subscribe(str => log(s"Received: $str"))

  sleep(3.seconds)

  def src1: Observable[Long] =
    Observable.interval(300.milliseconds)

  def src2: Observable[Long] =
    Observable.interval(1.seconds)

}

object Example20 extends App {

  Example19.src2.withLatestFrom(Example19.src1)((n2, n1) => s"Source2: $n2 Source 1: $n1")
    .subscribe(str => log(s"Received: $str"))

  sleep(3.seconds)

}
