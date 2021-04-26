package org.learning.reactive.extensions.testing

import org.junit.Assert.assertTrue
import org.junit.Test
import org.learning.reactive.extensions.core.{Observable, log, sleep}

import java.util.concurrent.atomic.AtomicInteger
import scala.concurrent.duration._

class BlockingSubscribersExamples {

  @Test
  def demoCode(): Unit = {
    Observable.interval(1.second)
      .take(count = 5)
      .subscribe(l => log(msg = s"Received: $l"))

    sleep(7.seconds)
  }

  @Test
  def testBlockingSubscribe(): Unit = {
    val hitCount = new AtomicInteger(0)

    Observable.interval(1.second)
      .take(count = 5)
      .blockingSubscribe(_ => hitCount.incrementAndGet())

    assertTrue(s"actual count = ${hitCount.get}", hitCount.get == 5)

  }

}
