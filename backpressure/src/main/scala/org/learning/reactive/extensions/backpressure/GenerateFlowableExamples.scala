package org.learning.reactive.extensions.backpressure

import io.reactivex.rxjava3.schedulers.Schedulers
import org.learning.reactive.extensions.core.{Flowable, log, sleep}

import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.atomic.AtomicInteger
import scala.concurrent.duration._

object Example15 extends App {

  randomNumbers(min = 1, max = 10000)
    .subscribeOn(Schedulers.computation())
    .doOnNext(n => log(msg = s"Emitting: $n"))
    .observeOn(Schedulers.io())
    .subscribe { n =>
      sleep(50.milliseconds)
      log(msg = s"Received: $n")
    }

  sleep(7.seconds)

  def randomNumbers(min: Int, max: Int): Flowable[Int] = Flowable.generate { emitter =>
    emitter onNext ThreadLocalRandom.current().nextInt(min, max)
  }

}

object Example16 extends App {

  rangeReverse(upperBound = 100, lowerBound = -100)
    .subscribeOn(Schedulers.computation())
    .doOnNext(n => log(msg = s"Emitting: $n"))
    .observeOn(Schedulers.io())
    .subscribe { n =>
      sleep(50.milliseconds)
      log(msg = s"Received: $n")
    }

  sleep(12.seconds)

  def rangeReverse(upperBound: Int, lowerBound: Int): Flowable[Int] =
    Flowable.generate(initialState = new AtomicInteger(upperBound + 1)) {
      case (state, emitter) =>
        val number = state.decrementAndGet
        emitter onNext number
        if (number == lowerBound) emitter.onComplete()
    }

}
