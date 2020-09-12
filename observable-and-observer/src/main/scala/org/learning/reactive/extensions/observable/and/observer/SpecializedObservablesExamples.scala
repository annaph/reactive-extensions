package org.learning.reactive.extensions.observable.and.observer

import org.learning.reactive.extensions.core._

object Example28 extends App {

  Single.just("Hello!")
    .map(_.length)
    .subscribe(
      n => log(s"$n"),
      e => log(s"Error captured: $e"))

}

object Example29 extends App {

  val source = Observable.just("Alpha", "Beta")

  source.first("Nil").subscribe(log)

}

object Example30 extends App {

  // has emission
  val source = Maybe just 100
  source.subscribe(
    n => log(s"Process 1: $n"),
    e => log(s"Error captured: $e"),
    log("Process 1 done!"))

  // no emission
  val empty = Maybe.empty[Int]()
  empty.subscribe(
    n => log(s"Process 2: $n"),
    e => log(s"Error captured: $e"),
    log("Process 2 done!"))

}

object Example31 extends App {

  // has emission
  val source = Observable just 100
  source.subscribe(
    n => log(s"Process 1: $n"),
    e => log(s"Error captured: $e"),
    log("Process 1 done!"))

  // no emission
  val empty = Observable.empty[Int]()
  empty.subscribe(
    n => log(s"Process 2: $n"),
    e => log(s"Error captured: $e"),
    log("Process 2 done!"))

}

object Example32 extends App {

  val source = Observable.just("Alpha", "Beta")

  source.firstElement().subscribe(
    s => log(s"RECEIVED $s"),
    e => log(s"Error captured: $e"),
    log("Done!"))

}

object Example33 extends App {

  Completable.fromRunnable(log("Doing some processing."))
    .subscribe(log("Done!"))

}
