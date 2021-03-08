package org.learning.reactive.extensions.backpressure

import io.reactivex.rxjava3.core.BackpressureOverflowStrategy
import io.reactivex.rxjava3.schedulers.Schedulers
import org.learning.reactive.extensions.core.{Flowable, log, sleep}

import scala.concurrent.duration._

object Example11 extends App {

  Flowable.interval(1.millisecond)
    .onBackpressureBuffer()
    .observeOn(Schedulers.io())
    .subscribe { n =>
      sleep(5.milliseconds)
      log(msg = s"$n")
    }

  sleep(7.seconds)

}

object Example12 extends App {

  Flowable.interval(1.millisecond)
    .onBackpressureBuffer(
      capacity = 10,
      onOverflow = log(msg = "overflow!"),
      overflowStrategy = BackpressureOverflowStrategy.DROP_LATEST)
    .observeOn(Schedulers.io())
    .subscribe { n =>
      sleep(5.milliseconds)
      log(msg = s"$n")
    }

  sleep(1.second)

}

object Example13 extends App {

  Flowable.interval(1.millisecond)
    .onBackpressureLatest()
    .observeOn(Schedulers.io())
    .subscribe { n =>
      sleep(5.milliseconds)
      log(msg = s"$n")
    }

  sleep(1.second)

}

object Example14 extends App {

  Flowable.interval(1.millisecond)
    .onBackpressureDrop(n => log(msg = s"Dropping $n"))
    .observeOn(Schedulers.io())
    .subscribe { n =>
      sleep(5.milliseconds)
      log(msg = s"$n")
    }

  sleep(1.second)

}
