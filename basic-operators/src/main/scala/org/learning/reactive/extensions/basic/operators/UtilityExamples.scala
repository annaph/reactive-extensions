package org.learning.reactive.extensions.basic.operators

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

import io.reactivex.rxjava3.schedulers.Timed
import org.learning.reactive.extensions.core.{Observable, log, sleep}

import scala.concurrent.duration._

object Example63 extends App {

  val formatter = DateTimeFormatter ofPattern "MM:ss"

  log(s"${LocalDateTime.now() format formatter}")

  Observable.just("Alpha", "Beta", "Gamma")
    .delay(3.seconds)
    .subscribe(s => log(s"${LocalDateTime.now() format formatter} Received: $s"))

  sleep(7.seconds)

}

object Example64 extends App {

  Observable.just("Alpha", "Beta", "Gamma")
    .repeat(2)
    .subscribe(s => log(s"Received: $s"))

}

object Example65 extends App {

  Observable.just("One")
    .single("Four")
    .subscribe(s => log(s"Received: $s"))

}

object Example66 extends App {

  Observable.just("One", "Two", "Three")
    .filter(_ contains 'z')
    .single("Four")
    .subscribe(s => log(s"Received: $s"))

}

object Example67 extends App {

  Observable.just("One", "Two", "Three")
    .timestamp(TimeUnit.SECONDS)
    .subscribe(t => log(s"Received: $t"))

}

object Example68 extends App {

  Observable.just("One", "Two", "Three")
    .timestamp(TimeUnit.SECONDS)
    .subscribe(t => log(s"Received: ${t.time} ${t.unit} ${t.value}"))

}

object Example69 extends App {

  source.subscribe(
    t => log(s"Received: $t"))

  sleep(12.seconds)

  def source: Observable[Timed[Long]] =
    Observable.interval(2.seconds)
      .doOnNext(n => log(s"Emitted: $n"))
      .take(3)
      .timeInterval(TimeUnit.SECONDS)

}

object Example70 extends App {

  Example69.source.subscribe(
    t => log(s"Received: ${t.time} ${t.unit} ${t.value}"))

  sleep(12.seconds)

}
