package org.learning.reactive.extensions.buffering

import java.util

import org.learning.reactive.extensions.core.{Observable, log, sleep}

import scala.concurrent.duration._
import scala.jdk.CollectionConverters._

object Example1 extends App {

  Observable.range(start = 1, count = 51)
    .buffer(count = 8)
    .map(_.mkString("[", ", ", "]"))
    .subscribe(l => log(msg = s"Received: $l"))

}

object Example2 extends App {

  Observable.range(start = 1, count = 51)
    .buffer(count = 8, bufferSupplier = new util.HashSet[Int])
    .map(_.asScala)
    .subscribe(s => log(msg = s"Received: $s"))

}

object Example3 extends App {

  Observable.range(start = 1, count = 12)
    .buffer(count = 2, skip = 3)
    .map(_.mkString("[", ", ", "]"))
    .subscribe(l => log(msg = s"Received: $l"))

}

object Example4 extends App {

  Observable.range(start = 1, count = 12)
    .buffer(count = 3, skip = 1)
    .map(_.mkString("[", ", ", "]"))
    .subscribe(l => log(msg = s"Received: $l"))

}

object Example5 extends App {

  Observable.range(start = 1, count = 12)
    .buffer(count = 2, skip = 1)
    .filter(_.tail.nonEmpty)
    .map(_.mkString("[", ", ", "]"))
    .subscribe(l => log(msg = s"Received: $l"))

}

object Example6 extends App {

  Observable.interval(300.milliseconds)
    .map(n => (n + 1) * 300)
    .buffer(1.second)
    .map(_.mkString("[", ", ", "]"))
    .subscribe(l => log(msg = s"Received: $l"))

  sleep(7.seconds)

}

object Example7 extends App {

  Observable.interval(300.milliseconds)
    .map(n => (n + 1) * 300)
    .buffer(timespan = 1.second, count = 2)
    .map(_.mkString("[", ",", "]"))
    .subscribe(l => log(msg = s"Received: $l"))

  sleep(7.seconds)

}

object Example8 extends App {

  val cutOffs = Observable.interval(1.second)

  Observable.interval(300.milliseconds)
    .map(n => (n + 1) * 300)
    .buffer(cutOffs)
    .map(_.mkString("[", ", ", "]"))
    .subscribe(l => log(msg = s"Received: $l"))

  sleep(7.seconds)

}
