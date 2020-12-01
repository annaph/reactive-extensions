package org.learning.reactive.extensions.concurrency

import java.util.concurrent.Executors

import io.reactivex.rxjava3.schedulers.Schedulers
import org.learning.reactive.extensions.core._

import scala.concurrent.duration._
import scala.util.{Failure, Success}

object Example7 extends App {

  val numberOfThreads = 20
  val executor = Executors newFixedThreadPool numberOfThreads
  val scheduler = Schedulers from executor

  Observable.from(items = "Alpha", "Beta", "Gamma", "Delta", "Epsilon")
    .subscribeOn(scheduler)
    .doFinally(executor.shutdown())
    .subscribe(str => log(msg = s"Received: $str"))

}

object Example8 extends App {

  val src = Observable.from(items = "Alpha", "Beta", "Gamma", "Delta", "Epsilon")
    .subscribeOn(Schedulers.computation())
    .map(intenseCalculation)
    .map(_.length)

  src.subscribe(n => log(msg = s"Observer 1: $n"))
  src.subscribe(n => log(msg = s"Observer 2: $n"))

  sleep(12.seconds)

}

object Example9 extends App {

  val src = Observable.from(items = "Alpha", "Beta", "Gamma", "Delta", "Epsilon")
    .subscribeOn(Schedulers.computation())
    .map(intenseCalculation)
    .map(_.length)
    .publish()
    .autoConnect(numberOfSubscribers = 2)

  src.subscribe(n => log(msg = s"Observer 1: $n"))
  src.subscribe(n => log(msg = s"Observer 2: $n"))

  sleep(12.seconds)

}

object Example10 extends App {

  val href = "https://api.github.com/users/annaph/starred"

  Observable.fromCallable(hrefResponse(href))
    .subscribeOn(Schedulers.io())
    .subscribe(str => log(msg = s"Received: $str"))

  sleep(12.seconds)

}

object Example11 extends App {

  Observable.interval(1.second)
    .subscribeOn(Schedulers.newThread())
    .subscribe(n => log(msg = s"Received: $n"))

  sleep(7.seconds)

}

object Example12 extends App {

  Observable.interval(1.second, Schedulers.newThread())
    .subscribe(n => log(msg = s"Received: $n"))

  sleep(7.seconds)

}

object Example13 extends App {

  Observable.from(items = "Alpha", "Beta", "Gamma", "Delta", "Epsilon")
    .subscribeOn(Schedulers.computation())
    .filter(_.length == 5)
    .subscribeOn(Schedulers.io())
    .subscribe(str => log(msg = s"Received: $str"))

  sleep(3.seconds)

}

object Example14 extends App {

  Observable.just(item1 = "WHISKEY/27653/TANGO", item2 = "6555/BRAVO", item3 = "232352/5675675/FOXTROT")
    .subscribeOn(Schedulers.io())
    .flatMap(str => Observable.from(str.split("/").toList))
    .observeOn(Schedulers.computation())
    .filter(_ matches "[0-9]+")
    .map(_.toInt)
    .fold(_ + _)
    .subscribe(n => log(msg = s"Received: $n"))

  sleep(3.seconds)

}

object Example15 extends App {

  Observable.just(item1 = "WHISKEY/11223/TANGO", item2 = "6555/BRAVO", item3 = "232352/5675675/FOXTROT")
    .subscribeOn(Schedulers.io())
    .flatMap(str => Observable.from(str.split("/").toList))
    .doOnNext(str => log(msg = s"Split out $str"))
    .observeOn(Schedulers.computation())
    .filter(_ matches "[0-9]+")
    .map(_.toInt)
    .fold(_ + _)
    .doOnSuccess(n => log(msg = s"Calculated sum $n"))
    .observeOn(Schedulers.io())
    .map(_.toString)
    .doOnSuccess(str => log(msg = s"Writing $str to file"))
    .subscribe { str =>
      writeToDisk(text = str, path = "output.txt") match {
        case Success(_) =>
          log(msg = "Written to file with success")
        case Failure(e) =>
          e.printStackTrace()
      }
    }

  sleep(3.seconds)

}
