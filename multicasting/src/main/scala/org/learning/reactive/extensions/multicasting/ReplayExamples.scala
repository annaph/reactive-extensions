package org.learning.reactive.extensions.multicasting

import org.learning.reactive.extensions.core.{Observable, log, sleep}

import scala.concurrent.duration._

object Example9 extends App {

  val ints = Observable.interval(1.seconds)
    .replay()
    .autoConnect()

  // Observer 1
  ints.subscribe(n => log(s"Observer 1: $n"))

  sleep(3.seconds)

  // Observer 2
  ints.subscribe(n => log(s"Observer 2: $n"))

  sleep(3.seconds)

}

object Example10 extends App {

  val src = Observable.just("Alpha", "Beta", "Gamma")
    .replay(bufferSize = 1)
    .autoConnect()

  // Observer 1
  src.subscribe(str => log(s"Observer 1: $str"))

  // Observer 2
  src.subscribe(str => log(s"Observer 2: $str"))

}

object Example11 extends App {

  val src = Observable.interval(300.milliseconds)
    .map(n => s"${(n + 1) * 300}")
    //.replay(1.seconds)
    .replay(bufferSize = 2, 1.seconds)
    .autoConnect()

  // Observer 1
  src.subscribe(str => log(s"Observer 1: $str"))

  sleep(2.seconds)

  // Observer 2
  src.subscribe(str => log(s"Observer 2: $str"))

  sleep(1.seconds)

}
