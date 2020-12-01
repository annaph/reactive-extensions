package org.learning.reactive.extensions.concurrency

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import io.reactivex.rxjava3.schedulers.Schedulers
import org.learning.reactive.extensions.core.{Observable, intenseCalculation, log, sleep}

import scala.concurrent.duration._

object Example1 extends App {

  val formatter = DateTimeFormatter ofPattern "mm:ss"
  log(msg = s"${LocalDateTime.now() format formatter}")

  Observable.interval(1.second)
    .map(n => s"${LocalDateTime.now() format formatter} $n Mississippi")
    .subscribe(log _)

  sleep(3.seconds)

}

object Example2 extends App {

  Observable.just(item1 = "Alpha", item2 = "Beta", item3 = "Gama")
    .map(intenseCalculation)
    .subscribe(str => log(s"Observer 1: $str"))

  Observable.range(start = 1, count = 3)
    .map(intenseCalculation)
    .subscribe(str => log(msg = s"Observer 2: $str"))

}

object Example3 extends App {

  Observable.just(item1 = "Alpha", item2 = "Beta", item3 = "Gama")
    .subscribeOn(Schedulers.computation())
    .map(intenseCalculation)
    .subscribe(str => log(s"Observer 1: $str"))

  Observable.range(start = 1, count = 3)
    .subscribeOn(Schedulers.computation())
    .map(intenseCalculation)
    .subscribe(str => log(msg = s"Observer 2: $str"))

  sleep(12.seconds)
}

object Example4 extends App {

  val src1 = Observable.just(item1 = "Alpha", item2 = "Beta", item3 = "Gamma")
    .subscribeOn(Schedulers.computation())
    .map(intenseCalculation)

  val src2 = Observable.range(start = 1, count = 3)
    .subscribeOn(Schedulers.computation())
    .map(intenseCalculation)

  Observable.zip(src1, src2)((str1, str2) => s"$str1-$str2")
    .subscribe(log _)

  sleep(12.seconds)
}

object Example5 extends App {

  Observable.interval(1.second)
    .map(intenseCalculation)
    .subscribe(n => log(s"Received: $n"))

  sleep(Long.MaxValue.nanoseconds)

}

object Example6 extends App {

  Observable.from(items = "Alpha", "Beta", "Gamma", "Delta", "Epsilon")
    .subscribeOn(Schedulers.computation())
    .map(intenseCalculation)
    .blockingSubscribe(
      str => log(msg = s"Received: $str"),
      _.printStackTrace(),
      log(msg = "Done!"))

}
