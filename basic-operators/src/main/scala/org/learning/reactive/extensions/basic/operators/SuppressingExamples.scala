package org.learning.reactive.extensions.basic.operators

import java.time.LocalDateTime

import org.learning.reactive.extensions.core.{Observable, log, sleep}

import scala.concurrent.duration._

object Example5 extends App {

  Observable.just("Alpha", "Beta", "Gamma")
    .filter(_.length != 5)
    .subscribe(s => log(s"RECEIVED: $s"))

}

object Example6 extends App {

  Observable.just("Alpha", "Beta", "Gamma")
    .take(2)
    .subscribe(s => log(s"RECEIVED: $s"))

}

object Example7 extends App {

  Observable.interval(300.millis)
    .take(2.seconds)
    .subscribe(n => log(s"${LocalDateTime.now()} RECEIVED: $n"))

  sleep(7.seconds)

}

object Example8 extends App {

  Observable.range(1, 100)
    .drop(90)
    .subscribe(n => log(s"RECEIVED: $n"))

}

object Example9 extends App {

  Observable.just("Alpha", "Beta", "Gamma")
    .map(_.length)
    .distinct()
    .subscribe(n => log(s"RECEIVED $n"))

}

object Example10 extends App {

  Observable.just("Alpha", "Beta", "Gamma")
    .distinct(_.length)
    .subscribe(s => log(s"RECEIVED: $s"))

}

object Example11 extends App {

  Observable.from(List(1, 1, 1, 2, 2, 3, 3, 2, 1, 1))
    .distinctUntilChanged()
    .subscribe(n => log(s"RECEIVED: $n"))

}

object Example12 extends App {

  Observable.from(List("Alpha", "Beta", "Zeta", "Eta", "Gamma", "Delta"))
    .distinctUntilChanged(_.length)
    .subscribe(s => log(s"RECEIVED: $s"))

}

object Example13 extends App {

  Observable.from(List("Alpha", "Beta", "Zeta", "Eta", "Gamma"))
    .elementAt(3)
    .subscribe(s => log(s"RECEIVED: $s"))

}
