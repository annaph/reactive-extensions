package org.learning.reactive.extensions.multicasting

import java.util.concurrent.ThreadLocalRandom

import org.learning.reactive.extensions.core.{Observable, log, sleep}

import scala.concurrent.duration._

object Example6 extends App {

  val randomInts = Observable.
    range(start = 1, count = 3)
    .map(_ => ThreadLocalRandom.current() nextInt 100)
    .publish()
    .autoConnect(numberOfSubscribers = 2)

  // Observer 1
  randomInts.subscribe(n => log(s"Observer 1: $n"))

  // Observer 2
  randomInts.foldLeft(0)(_ + _)
    .subscribe(n => log(s"Observer 2: $n"))

  // Observer 3 - receives nothing
  randomInts.subscribe(n => log(s"Observer 3: $n"))

}

object Example7 extends App {

  val ints = Observable.interval(1.seconds)
    .publish()
    .autoConnect()

  // Observer 1
  ints.subscribe(n => log(s"Observer 1: $n"))

  sleep(3.seconds)

  // Observer 2
  ints.subscribe(n => log(s"Observer 2: $n"))

  sleep(3.seconds)

}

object Example8 extends App {

  val ints = Observable.interval(1.seconds)
    .publish()
    .refCount()

  // Observer 1
  ints.take(count = 5)
    .subscribe(n => log(s"Observer 1: $n"))

  sleep(3.seconds)

  // Observer 2
  ints.take(count = 2)
    .subscribe(n => log(s"Observer 2: $n"))

  sleep(3.seconds)

  // Observer 3
  ints.subscribe(n => log(s"Observer 3: $n"))

  sleep(3.seconds)

}
