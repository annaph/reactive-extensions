package org.learning.reactive.extensions.buffering

import org.learning.reactive.extensions.core.{Observable, log, sleep}

import scala.concurrent.duration._

object Example13 extends App {

  Observable.concat(source1, source2, source3)
    .subscribe(log _)

  sleep(7.seconds)

  def source1: Observable[String] = Observable.interval(100.milliseconds)
    .map(n => (n + 1) * 100)
    .map(n => s"SOURCE 1: $n")
    .take(count = 10)

  def source2: Observable[String] = Observable.interval(300.milliseconds)
    .map(n => (n + 1) * 300)
    .map(n => s"SOURCE 2: $n")
    .take(count = 3)

  def source3: Observable[String] = Observable.interval(2.seconds)
    .map(n => (n + 1) * 2000)
    .map(n => s"SOURCE 3: $n")
    .take(count = 2)

}

object Example14 extends App {

  import Example13._

  Observable.concat(source1, source2, source3)
    .throttleLast(1.second)
    .subscribe(log _)

  sleep(7.seconds)

}

object Example15 extends App {

  import Example13._

  Observable.concat(source1, source2, source3)
    .throttleLast(2.seconds)
    .subscribe(log _)

  sleep(7.seconds)

}

object Example16 extends App {

  import Example13._

  Observable.concat(source1, source2, source3)
    .throttleLast(500.milliseconds)
    .subscribe(log _)

  sleep(7.seconds)

}

object Example17 extends App {

  import Example13._

  Observable.concat(source1, source2, source3)
    .throttleFirst(1.second)
    .subscribe(log _)

  sleep(7.seconds)

}

object Example18 extends App {

  import Example13._

  Observable.concat(source1, source2, source3)
    .throttleWithTimeout(1.second)
    .subscribe(log _)

  sleep(7.seconds)

}
