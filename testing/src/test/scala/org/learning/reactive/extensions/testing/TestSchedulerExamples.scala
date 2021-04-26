package org.learning.reactive.extensions.testing

import org.junit.Test
import org.learning.reactive.extensions.core.Observable
import org.learning.reactive.extensions.core.testing.{TestObserver, TestScheduler}

import scala.concurrent.duration._

class TestSchedulerExamples {

  @Test
  def usingTestScheduler(): Unit = {
    // Declare TestScheduler
    val testScheduler = TestScheduler()

    // Declare TestObserver
    val testObserver = TestObserver.create[Long]

    // Declare Observable
    val source = Observable.interval(period = 1.minute, testScheduler)

    // Subscribe TestObserver to Observable
    source subscribe testObserver

    // Fast forwards by 30 seconds
    testScheduler advanceTimeBy 30.seconds

    // Assert no emissions have occurred yet
    testObserver assertValueCount 0

    // Fast forward to 70 seconds after subscription
    testScheduler advanceTimeTo 70.seconds

    // Assert the first emission has occurred
    testObserver assertValueCount 1

    // Fast forward to 90 minutes after subscription
    testScheduler advanceTimeTo 90.minutes

    // Assert 90 emissions have occurred
    testObserver assertValueCount 90

  }

}
