package org.learning.reactive.extensions.core.testing

import io.reactivex.rxjava3.observers.{TestObserver => RxTestObserver}

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration
import scala.jdk.CollectionConverters._
import scala.reflect.ClassTag

class TestObserver[T](val rxTestObserver: RxTestObserver[T]) {

  def assertComplete: TestObserver[T] = TestObserver {
    rxTestObserver.assertComplete()
  }

  def assertNotComplete: TestObserver[T] = TestObserver {
    rxTestObserver.assertNotComplete()
  }

  def assertNoErrors: TestObserver[T] = TestObserver {
    rxTestObserver.assertNoErrors()
  }

  def assertValues(values: T*)(implicit ct: ClassTag[T]): TestObserver[T] = TestObserver {
    rxTestObserver.assertValues(values: _*)
  }

  def assertValueCount(count: Int): TestObserver[T] = TestObserver {
    rxTestObserver assertValueCount count
  }

  def await(period: Duration): Boolean =
    rxTestObserver.await(period.toMillis, TimeUnit.MILLISECONDS)

  def hasSubscription: Boolean =
    rxTestObserver.hasSubscription
    
  def values: List[T] =
    rxTestObserver.values().asScala.toList

}

object TestObserver {

  def apply[T](rxTestObserver: RxTestObserver[T]): TestObserver[T] =
    new TestObserver[T](rxTestObserver)

  def create[T]: TestObserver[T] = TestObserver {
    RxTestObserver.create()
  }

}
