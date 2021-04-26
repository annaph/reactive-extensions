package org.learning.reactive.extensions.testing

import org.junit.Assert.assertTrue
import org.junit.Test
import org.learning.reactive.extensions.core.{Observable, log}

import scala.concurrent.duration._

class BlockingOperatorsExamples {

  @Test
  def testBlockingFirst(): Unit = {
    val source = Observable.from(items = "Alpha", "Beta", "Gamma", "Delta", "Zeta")

    val firstWithLengthFour = source
      .filter(_.length == 4)
      .blockingFirst()

    assertTrue(firstWithLengthFour == "Beta")

  }

  @Test
  def testBlockingGet(): Unit = {
    val source = Observable.from(items = "Alpha", "Beta", "Gamma", "Delta", "Zeta")

    val allFourLength = source
      .filter(_.length == 4)
      .toList
      .blockingGet()

    assertTrue(allFourLength == List("Beta", "Zeta"))

  }

  @Test
  def testBlockingLast(): Unit = {
    val source = Observable.from(items = "Alpha", "Beta", "Gamma", "Delta", "Zeta", "Epsilon")

    val lastLengthFour = source
      .filter(_.length == 4)
      .blockingLast()

    assertTrue(lastLengthFour == "Zeta")

  }

  @Test
  def testBlockingIterable(): Unit = {
    val source = Observable.from(items = "Alpha", "Beta", "Gamma", "Delta", "Zeta")

    val allLengthFive = source
      .filter(_.length == 5)
      .blockingIterable()

    for (str <- allLengthFive) assertTrue(str.length == 5)

  }

  @Test
  def testBlockingForEach(): Unit = {
    val source = Observable.from(items = "Alpha", "Beta", "Gamma", "Delta", "Zeta")

    source.filter(_.length == 5).blockingForEach { str =>
      assertTrue(str.length == 5)
    }

  }

  @Test
  def testBlockingNext(): Unit = {
    val source = Observable.interval(1.microsecond)
      .take(count = 1000)

    val iterable = source.blockingNext()

    for (l <- iterable) log(msg = s"$l")

  }

  @Test
  def testBlockingLatest(): Unit = {
    val source = Observable.interval(1.microsecond)
      .take(count = 1000)

    val iterable = source.blockingLatest()

    for (l <- iterable) log(msg = s"$l")

  }

  @Test
  def testBlockingMostRecent(): Unit = {
    val source = Observable.interval(period = 10.milliseconds)
      .take(count = 5)

    val iterable = source.blockingMostRecent(initialItem = -1)

    for (l <- iterable) log(msg = s"$l")

  }

}
