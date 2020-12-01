package org.learning.reactive.extensions.concurrency

import java.time.LocalTime
import java.util.concurrent.atomic.AtomicInteger

import io.reactivex.rxjava3.schedulers.Schedulers
import org.learning.reactive.extensions.core.{Observable, intenseCalculation, log, sleep}

import scala.concurrent.duration._

object Example16 extends App {

  Observable.range(start = 1, count = 12)
    .map(intenseCalculation)
    .subscribe(n => log(msg = s"Received: $n ${LocalTime.now()}"))

  sleep(31.seconds)

}

object Example17 extends App {

  Observable.range(start = 1, count = 12)
    .flatMap(n => Observable.just(n)
      .subscribeOn(Schedulers.computation())
      .map(intenseCalculation))
    .subscribe(n => log(msg = s"Received: $n ${LocalTime.now()}"))

  sleep(31.seconds)

}

object Example18 extends App {

  val numberOfCores = Runtime.getRuntime.availableProcessors
  val assigner = new AtomicInteger

  Observable.range(start = 1, count = 23)
    .groupBy(_ => assigner.incrementAndGet() % numberOfCores)
    .flatMap(_.observeOn(Schedulers.io()).map(intenseCalculation))
    .subscribe(n => log(msg = s"Received: $n ${LocalTime.now()}"))

  sleep(12.seconds)

}

object Example19 extends App {

  val disposable = Observable.interval(1.seconds)
    .doOnDispose(log(msg = "Disposing"))
    .subscribe(n => log(msg = s"Received: $n"))

  sleep(3.seconds)
  disposable.dispose()
  sleep(3.seconds)

}

object Example20 extends App {

  val disposable = Observable.interval(1.seconds)
    .doOnDispose(log(msg = "Disposing"))
    .unsubscribeOn(Schedulers.io())
    .subscribe(n => log(msg = s"Received: $n"))

  sleep(3.seconds)
  disposable.dispose()
  sleep(3.seconds)

}
