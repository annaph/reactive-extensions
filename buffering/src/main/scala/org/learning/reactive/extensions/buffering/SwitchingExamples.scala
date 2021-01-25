package org.learning.reactive.extensions.buffering

import org.learning.reactive.extensions.core.{Observable, log, randomDuration, sleep}

import scala.concurrent.duration._

object Example19 extends App {

  source.subscribe(log _)

  sleep(24.seconds)

  def source: Observable[String] = Observable
    .from(items = "Alpha", "Beta", "Gamma", "Delta", "Epsilon", "Zeta", "Eta", "Theta", "Iota")
    .concatMap(str =>
      Observable.just(str).delay(randomDuration()))

}

object Example20 extends App {

  import Example19._

  Observable.interval(5.seconds)
    .switchMap(_ =>
      source doOnDispose log(msg = "Disposing! Starting next"))
    .subscribe(log _)

  sleep(24.seconds)

}
