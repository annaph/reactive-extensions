package org.learning.reactive.extensions.core.testing

import io.reactivex.rxjava3.schedulers.{TestScheduler => RxTestScheduler}

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration

class TestScheduler(val rxTestScheduler: RxTestScheduler) {

  def advanceTimeBy(delayTime: Duration): Unit =
    rxTestScheduler.advanceTimeBy(delayTime.toMillis, TimeUnit.MILLISECONDS)

  def advanceTimeTo(delayTime: Duration): Unit =
    rxTestScheduler.advanceTimeTo(delayTime.toMillis, TimeUnit.MILLISECONDS)

}

object TestScheduler {

  def apply(): TestScheduler = new TestScheduler(new RxTestScheduler)

}
