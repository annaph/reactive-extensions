package org.learning.reactive.extensions.basic.operators

import org.learning.reactive.extensions.core.{Observable, log}

import scala.util.{Failure, Success, Try}

object Example41 extends App {

  Observable.from(List(5, 2, 4, 0, 3))
    .map(10 / _)
    .subscribe(
      n => log(s"Received: $n"),
      e => log(s"Error: $e"))

}

object Example42 extends App {

  Observable.from(List(5, 2, 4, 0, 3))
    .map(10 / _)
    .onErrorReturnItem(-1)
    .subscribe(
      n => log(s"Received: $n"),
      e => log(s"Error: $e"))

}

object Example43 extends App {

  Observable.from(List(5, 2, 4, 0, 13))
    .map(10 / _)
    .onErrorReturn(handleFailure)
    .subscribe(
      n => log(s"Received: $n"),
      e => log(s"Error: $e"))

  def handleFailure(e: Throwable): Int = e match {
    case _: ArithmeticException =>
      -1
    case _ =>
      0
  }

}

object Example44 extends App {

  Observable.from(List(5, 2, 4, 0, 3))
    .map(divide)
    .subscribe(
      n => log(s"Received: $n"),
      e => log(s"Error: $e"))

  def divide(n: Int): Int = Try(10 / n) match {
    case Success(value) =>
      value
    case Failure(_: ArithmeticException) =>
      -1
    case _ =>
      0
  }

}

object Example45 extends App {

  Observable.from(List(5, 2, 4, 0, 3))
    .map(10 / _)
    .onErrorResumeWith(Observable.just(-1).repeat(3))
    .subscribe(
      n => log(s"Received: $n"),
      e => log(s"Error: $e"))

}

object Example46 extends App {

  Observable.from(List(5, 2, 4, 0, 3))
    .map(10 / _)
    .onErrorResumeWith(Observable.empty())
    .subscribe(
      n => log(s"Received: $n"),
      e => log(s"Error: $e"),
      log("Done!"))

}

object Example47 extends App {

  Observable.from(List(5, 2, 4, 0, 3))
    .map(10 / _)
    .onErrorResumeNext(_ => Observable.just(-1).repeat(3))
    .subscribe(
      n => log(s"Received: $n"),
      e => log(s"Error: $e"))

}

object Example48 extends App {

  Observable.from(List(5, 2, 4, 0, 3))
    .map(10 / _)
    .retry()
    .subscribe(
      n => log(s"Received: $n"),
      e => log(s"Error: $e"))

}

object Example49 extends App {

  Observable.from(List(5, 2, 4, 0, 3))
    .map(10 / _)
    .retry(2)
    .subscribe(
      n => log(s"Received: $n"),
      e => log(s"Error: $e"))

}
