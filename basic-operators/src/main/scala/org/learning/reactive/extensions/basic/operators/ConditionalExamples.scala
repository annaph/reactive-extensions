package org.learning.reactive.extensions.basic.operators

import org.learning.reactive.extensions.core.{Observable, log}

object Example1 extends App {

  Observable.range(1, 100)
    .takeWhile(_ < 5)
    .subscribe(n => log(s"RECEIVED: $n"))

}

object Example2 extends App {

  Observable.range(1, 100)
    .dropWhile(_ <= 95)
    .subscribe(n => log(s"RECEIVED: $n"))

}

object Example3 extends App {

  Observable.just("Alpha", "Beta")
    .filter(_ startsWith "Z")
    .defaultIfEmpty("None")
    .subscribe(log _)

}

object Example4 extends App {

  Observable.just("Alpha", "Beta", "Gamma")
    .filter(_ startsWith "Z")
    .switchIfEmpty(Observable.just("Zeta", "Eta", "Theta"))
    .subscribe(
      s => log(s"RECEIVED: $s"),
      e => log(s"RECEIVED ERROR: $e"))

}
