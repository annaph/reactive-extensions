package org.learning.reactive.extensions.multicasting

import java.util.concurrent.ThreadLocalRandom

import org.learning.reactive.extensions.core.{ConnectableObservable, Observable, log}

object Example1 extends App {

  val ints = Observable.range(1, 3).publish()

  ints.subscribe(n => log(s"Observer One: $n"))
  ints.subscribe(n => log(s"Observer Two: $n"))

  ints.connect()

}

object Example2 extends App {

  val ints = Observable
    .range(1, 3)
    .map(_ => ThreadLocalRandom.current() nextInt 100)

  ints.subscribe(n => log(s"Observer 1: $n"))
  ints.subscribe(n => log(s"Observer 2: $n"))

}

object Example3 extends App {

  val ints = Observable.
    range(start = 1, count = 3)
    .publish()

  val randomInts = ints.map(_ => ThreadLocalRandom.current() nextInt 100)

  randomInts.subscribe(n => log(s"Observer 1: $n"))
  randomInts.subscribe(n => log(s"Observer 2: $n"))

  ints.connect()

}

object Example4 extends App {

  val ints = randomInts()

  ints.subscribe(n => log(s"Observer 1: $n"))
  ints.subscribe(n => log(s"Observer 2: $n"))

  ints.connect()

  def randomInts(): ConnectableObservable[Int] = Observable.
    range(start = 1, count = 3)
    .map(_ => ThreadLocalRandom.current() nextInt 100)
    .publish()

}

object Example5 extends App {

  val ints = Example4.randomInts()

  // Observer 1
  ints.subscribe(n => log(s"Observer 1: $n"))

  // Observer 2
  ints.foldLeft(0)(_ + _)
    .subscribe(n => log(s"Observer 2: $n"))

  ints.connect()

}
