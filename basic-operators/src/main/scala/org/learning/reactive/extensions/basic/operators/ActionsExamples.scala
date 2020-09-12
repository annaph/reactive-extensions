package org.learning.reactive.extensions.basic.operators

import org.learning.reactive.extensions.core.{Observable, log, sleep}

import scala.concurrent.duration._

object Example50 extends App {

  Observable.just("Alpha", "Beta", "Gamma")
    .doOnNext(s => log(s"Processing: $s"))
    .map(_.length)
    .subscribe(n => log(s"Received: $n"))

}

object Example51 extends App {

  Observable.just("Alpha", "Beta", "Gamma")
    .doAfterNext(s => log(s"After: $s"))
    .map(_.length)
    .subscribe(n => log(s"Received: $n"))

}

object Example52 extends App {

  Observable.just("Alpha", "Beta", "Gamma")
    .doOnComplete(log("Source is done emitting!"))
    .map(_.length)
    .subscribe(n => log(s"Received: $n"))

}

object Example53 extends App {

  Observable.from(List(5, 2, 4, 0, 3, 2, 8))
    .doOnError(_ => log("Source failed!"))
    .map(10 / _)
    .doOnError(_ => log("Division failed!"))
    .subscribe(
      n => log(s"Received: $n"),
      e => log(s"Error: $e"))

}

object Example54 extends App {

  Observable.just("One", "Two", "Three")
    .doOnEach(notification => log(s"doOnEach: $notification"))
    .subscribe(s => log(s"Received: $s"))

}

object Example55 extends App {

  Observable.just("One", "Two", "Three")
    .doOnEach(s => log(s"doOnEach: (${s.isOnNext}, ${s.isOnError}, ${s.isOnComplete})"))
    .subscribe(s => log(s"Received: $s"))

}

object Example56 extends App {

  Observable.just("One", "Two", "Three")
    .doOnEach(s => log(s"doOnEach: Error: '${s.getError}', Value: '${s.getValue}'"))
    .subscribe(s => log(s"Received: $s"))

}

object Example57 extends App {

  Observable.just("Alpha", "Beta", "Gamma")
    .doOnSubscribe(_ => log("Subscribing!"))
    .doOnDispose(log("Disposing!"))
    .subscribe(s => log(s"Received: $s"))

}

object Example58 extends App {

  val disposable = Observable.interval(1.seconds)
    .doOnSubscribe(_ => log("Subscribing!"))
    .doOnDispose(log("Disposing"))
    .doFinally(log("doFinally!"))
    .subscribe(s => log(s"Received: $s"))

  sleep(3.seconds)
  disposable.dispose()
  sleep(3.seconds)

}

object Example59 extends App {

  Observable.just(5, 3, 7)
    .fold(_ + _)
    .doOnSuccess(n => log(s"Emitting: $n"))
    .subscribe(n => log(s"Received: $n"))

}

object Example60 extends App {

  Observable.just("One", "Two", "Three")
    .doFinally(log("doFinally!"))
    .doAfterTerminate(log("doAfterTerminate!"))
    .subscribe(s => log(s"Received: $s"))

}

object Example61 extends App {

  val disposable = Observable.interval(1.seconds)
    .doOnSubscribe(_ => log("Subscribing!"))
    .doOnDispose(log("Disposing!"))
    .doFinally(log("doFinally!"))
    .doAfterTerminate(log("doAfterTerminate!"))
    .subscribe(n => log(s"Received: $n"))

  sleep(3.seconds)
  disposable.dispose()
  sleep(3.seconds)

}

object Example62 extends App {

  val disposable = Observable.interval(1.seconds)
    .doAfterTerminate(log("doAfterTerminate!"))
    .doFinally(log("doFinally!"))
    .doOnDispose(log("Disposing!"))
    .doOnSubscribe(_ => log("Subscribing!"))
    .subscribe(n => log(s"Received: $n"))

  sleep(3.seconds)
  disposable.dispose()
  sleep(3.seconds)

}
