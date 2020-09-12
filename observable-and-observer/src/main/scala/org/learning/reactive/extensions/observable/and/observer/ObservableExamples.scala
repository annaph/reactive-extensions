package org.learning.reactive.extensions.observable.and.observer

import org.learning.reactive.extensions.core.{Observable, log}

import scala.util.{Failure, Try}

object Example1 extends App {

  val source = Observable.create[String] { emitter =>
    emitter onNext "Alpha"
    emitter onNext "Beta"
    emitter onNext "Gamma"

    emitter.onComplete()
  }

  source.subscribe(s => log(s"RECEIVED: $s"))

}

object Example2 extends App {

  source().subscribe(s => log(s"RECEIVED: $s"), _.printStackTrace)

  def source(): Observable[String] = Observable.create[String] { emitter =>
    Try {
      emitter onNext "Alpha"
      emitter onNext "Beta"
      emitter onNext "Gamma"

      emitter.onComplete()
    } match {
      case Failure(e) =>
        emitter onError e
      case _ =>
    }
  }

}

object Example3 extends App {

  Example2.source()
    .map(_.length)
    .filter(_ >= 5)
    .subscribe(n => log(s"RECEIVED: $n"))

}

object Example4 extends App {

  val source = Observable.just("Alpha", "Beta", "Gamma")

  source
    .map(_.length)
    .filter(_ >= 5)
    .subscribe(n => log(s"RECEIVED: $n"))

}

object Example5 extends App {

  val items = List("Alpha", "Beta", "Gamma")

  Observable.from(items)
    .map(_.length)
    .filter(_ >= 5)
    .subscribe(n => log(s"RECEIVED: $n"))

}
