package org.learning.reactive.extensions.observable.and.observer

import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import org.learning.reactive.extensions.core.{Observable, log}

object Example6 extends App {

  val myObserver = new Observer[Int] {

    override def onSubscribe(d: Disposable): Unit = {}

    override def onNext(n: Int): Unit =
      log(s"RECEIVED: $n")

    override def onComplete(): Unit =
      log("Done!")

    override def onError(e: Throwable): Unit =
      e.printStackTrace()

  }

  source()
    .map(_.length)
    .filter(_ >= 5)
    .subscribe(myObserver)

  def source(): Observable[String] =
    Observable.just("Alpha", "Beta", "Gamma")

}

object Example7 extends App {

  Example6.source()
    .map(_.length)
    .filter(_ >= 5)
    .subscribe(
      n => log(s"RECEIVED: $n"),
      e => e.printStackTrace(),
      log("Done!")
    )

}

object Example8 extends App {

  Example6.source()
    .map(_.length)
    .filter(_ >= 5)
    .subscribe(
      n => log(s"RECEIVED: $n"),
      e => e.printStackTrace()
    )

}

object Example9 extends App {

  Example6.source()
    .map(_.length)
    .filter(_ >= 5)
    .subscribe(n => log(s"RECEIVED: $n"))

}

object Example10 extends App {

  val source = Observable.just("Alpha", "Beta", "Gamma")

  source.subscribe(s => log(s"Observer 1: $s"))
  source.subscribe(s => log(s"Observer 2: $s"))

}

object Example11 extends App {

  val source = Observable.just("Alpha", "Beta", "Gamma")

  source.subscribe(s => log(s"Observer 1: $s"))

  source
    .map(_.length)
    .filter(_ >= 5)
    .subscribe(n => log(s"Observer 2: $n"))

}

object Example12 extends App {

  val source = Observable
    .just("Alpha", "Beta", "Gamma")
    .publish()

  // Observer 1
  source.subscribe(s => log(s"Observer 1: $s"))

  // Observer 2
  source
    .map(_.length)
    .subscribe(n => log(s"Observer 2: $n"))

  // Fire!
  source.connect()

}
