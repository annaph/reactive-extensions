package org.learning.reactive.extensions.combining.observables

import org.learning.reactive.extensions.core.{Observable, log, sleep}

import scala.concurrent.duration._

object Example1 extends App {

  val src1 = Observable.just("Alpha", "Beta")
  val src2 = Observable.just("Zeta", "Eta")

  Observable.merge(src1, src2)
    .subscribe(s => log(s"Received: $s"))

}

object Example2 extends App {

  val src1 = Observable.just("Alpha", "Beta")
  val src2 = Observable.just("Zeta", "Eta")

  src1.mergeWith(src2).subscribe(s => log(s"Received: $s"))

}

object Example3 extends App {

  val src1 = Observable.just("Alpha", "Beta")
  val src2 = Observable.just("Gamma", "Delta")
  val src3 = Observable.just("Epsilon", "Zeta")
  val src4 = Observable.just("Eta", "Theta")
  val src5 = Observable.just("Iota", "Kappa")

  Observable.merge(src1, src2, src3, src4, src5).subscribe(s => log(s"Received: $s"))

}

object Example4 extends App {

  val src1 = Observable.just("Alpha", "Beta")
  val src2 = Observable.just("Gamma", "Delta")
  val src3 = Observable.just("Epsilon", "Zeta")
  val src4 = Observable.just("Eta", "Theta")
  val src5 = Observable.just("Iota", "Kappa")

  val sources = List(src1, src2, src3, src4, src5)

  Observable.merge(sources).subscribe(s => log(s"Received: $s"))

}

object Example5 extends App {

  val src1 = Observable.interval(1.seconds)
    .map(_ + 1)
    .map(n => s"Source1 $n seconds")

  val src2 = Observable.interval(300.milliseconds)
    .map(n => (n + 1) * 300)
    .map(n => s"Source2 $n milliseconds")

  Observable.merge(src1, src2)
    .subscribe(log _)

  sleep(3.seconds)

}

object Example6 extends App {

  Observable.just("Alpha", "Beta", "Gamma")
    .flatMap(s => Observable.from(s.split("").toSeq))
    .subscribe(s => log(s"Received: $s"))

}

object Example7 extends App {

  Observable.just("521934/2342/FOXTROT", "21962/12112/TANGO/78886")
    .flatMap(s => Observable.from(s.split('/').toSeq))
    .filter(_ matches "[0-9]+")
    .map(_.toInt)
    .subscribe(n => log(s"Received: $n"))

}

object Example8 extends App {

  Observable.from(2, 3, 10, 7)
    .flatMap(intervalSrc)
    .subscribe(log _)

  sleep(12.seconds)

  def intervalSrc(period: Int): Observable[String] =
    Observable.interval(period.seconds)
      .map(n => s"${period}s interval: ${(n + 1) * period} seconds elapsed")

}

object Example9 extends App {

  Observable.from(2, 0, 3, 10, 7)
    .flatMap(intervalSrc)
    .subscribe(log _)

  sleep(12.seconds)

  def intervalSrc(period: Int): Observable[String] = period match {
    case p if p <= 0 =>
      Observable.empty()
    case _ =>
      Example8 intervalSrc period
  }

}

object Example10 extends App {

  Observable.just("Alpha", "Beta", "Gamma")
    .flatMap[String, String](
      s => Observable.from(s.split("").toSeq),
      (str, ch) => s"$str-$ch")
    .subscribe(s => log(s"Received: $s"))

}
