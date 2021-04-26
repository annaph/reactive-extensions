package org.learning.reactive.extensions.observable.and.observer

import org.learning.reactive.extensions.core.{Observable, log, sleep}

import java.time.LocalDateTime
import scala.concurrent.duration._

object Example13 extends App {

  Observable
    .range(1, 3)
    .subscribe(n => log(s"RECEIVED: $n"))

}

object Example14 extends App {

  Observable
    .range(5, 3)
    .subscribe(n => log(s"RECEIVED: $n"))

}

object Example15 extends App {

  Observable
    .interval(1.seconds)
    .subscribe(n => log(s"${LocalDateTime.now().getSecond} $n Mississippi"))

  sleep(3.seconds)

}

object Example16 extends App {

  val source = Observable.interval(1.seconds)

  // Observer 1
  source.subscribe(n => log(s"Observer 1: $n"))

  sleep(3.seconds)

  // Observer 2
  source.subscribe(n => log(s"Observer 2: $n"))

  sleep(3.seconds)

}

object Example17 extends App {

  val source = Observable.interval(1.seconds).publish()

  // Observer 1
  source.subscribe(n => log(s"Observer 1: $n"))
  source.connect()

  sleep(3.seconds)

  // Observer 2
  source.subscribe(n => log(s"Observer 2: $n"))

  sleep(3.seconds)

}

object Example18 extends App {

  val empty = Observable.empty[String]

  empty.subscribe(
    log,
    _.printStackTrace(),
    log("Done!"))

}

object Example19 extends App {

  val never = Observable.never[String]()

  never.subscribe(
    log,
    _.printStackTrace(),
    log("Done!"))

  sleep(7.seconds)

}

object Example20 extends App {

  val source = Observable.error[String](new Exception("Crash and burn!"))

  source.subscribe(
    s => log(s"RECEIVED: $s"),
    e => log(s"Error captured: $e"),
    log("Done!"))

}

object Example21 extends App {

  val source = Observable.error[String](() => new Exception("Crash and burn!"))

  source.subscribe(
    s => log(s"RECEIVED: $s"),
    e => log(s"Error captured: $e"))

}

object Example22 extends App {

  val start = 1
  var count = 3

  val source = Observable.range(start, count)

  // Observer 1
  source.subscribe(n => log(s"Observer 1: $n"))

  count = 5

  // Observer 2
  source.subscribe(n => log(s"Observer 2: $n"))

}

object Example23 extends App {

  val start = 1
  var count = 3

  val source = Observable.defer(Observable.range(start, count))

  // Observer 1
  source.subscribe(n => log(s"Observer 1: $n"))

  count = 5

  // Observer 2
  source.subscribe(n => log(s"Observer 2: $n"))

}

object Example24 extends App {

  val source = Observable.from(List("Alpha", "Beta", "Gamma"))

  // Observer 1
  source.subscribe(s => log(s"RECEIVED: $s"))

  // Observer 2
  source.subscribe(s => log(s"RECEIVED: $s"))

}

object Example25 extends App {

  val source = Observable.just(1 / 0)

  source.subscribe(
    n => log(s"RECEIVED NUMBER: $n"),
    e => log(s"Error captured: $e"))

}

object Example26 extends App {

  val source = Observable.just(1)

  source
    .map(_ / 0)
    .subscribe(
      n => log(s"RECEIVED NUMBER: $n"),
      e => log(s"Error captured: $e"))

}

object Example27 extends App {

  val source = Observable.fromCallable(1 / 0)

  source.subscribe(
    n => log(s"RECEIVED NUMBER DIVISION: $n"),
    e => log(s"Error captured: $e"))

}
