package org.learning.reactive.extensions.buffering

import org.learning.reactive.extensions.core.{Observable, log, sleep}

import scala.concurrent.duration._

object Example9 extends App {

  Observable.range(start = 1, count = 51)
    .map(_.toLong)
    .window(count = 8)
    .flatMapSingle(_.foldLeft("")(foldNumber))
    .subscribe(str => log(msg = s"Received: $str"))

  def foldNumber(acc: String, n: Long): String =
    if (acc.nonEmpty) s"$acc|${n.toString}" else n.toString

}

object Example10 extends App {

  Observable.range(start = 1, count = 51)
    .map(_.toLong)
    .window(count = 2, skip = 3)
    .flatMapSingle(_.foldLeft("")(Example9.foldNumber))
    .subscribe(str => log(msg = s"Received: $str"))

}

object Example11 extends App {

  Observable.interval(300.milliseconds)
    .map(n => (n + 1) * 300)
    .window(1.second)
    .flatMapSingle(_.foldLeft("")(Example9.foldNumber))
    .subscribe(str => log(msg = s"Received: $str"))

  sleep(7.seconds)

}

object Example12 extends App {

  val cutOffs = Observable.interval(1.second)

  Observable.interval(300.milliseconds)
    .map(n => (n + 1) * 300)
    .window(cutOffs)
    .flatMapSingle(_.foldLeft("")(Example9.foldNumber))
    .subscribe(str => log(msg = s"Received: $str"))

  sleep(7.seconds)

}
