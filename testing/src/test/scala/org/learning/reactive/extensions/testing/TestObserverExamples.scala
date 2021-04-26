package org.learning.reactive.extensions.testing

import org.junit.Assert.{assertFalse, assertTrue}
import org.junit.Test
import org.learning.reactive.extensions.core.testing.TestObserver
import org.learning.reactive.extensions.core.{Observable, tryToExecute}

import scala.concurrent.duration._

class TestObserverExamples {

  @Test
  def usingTestObserver(): Unit = {
    val source = Observable.interval(period = 1.second)
      .take(count = 3)

    val testObserver = TestObserver.create[Long]
    assertFalse("Test observer should not have subscription", testObserver.hasSubscription)

    source subscribe testObserver
    assertTrue("Test observer should have subscription", testObserver.hasSubscription)

    tryToExecute(action = testObserver await 4.seconds)(onError = throw _)

    testObserver.assertComplete
    testObserver.assertNoErrors
    testObserver assertValueCount 3
    testObserver.assertValues(values = 0, 1, 2)

  }

}
