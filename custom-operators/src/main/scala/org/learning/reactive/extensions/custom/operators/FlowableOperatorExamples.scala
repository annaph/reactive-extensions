package org.learning.reactive.extensions.custom.operators

import io.reactivex.rxjava3.subscribers.DisposableSubscriber
import org.learning.reactive.extensions.core.Flowable.FlowableOperator
import org.learning.reactive.extensions.core.{Flowable, log, tryToExecute}
import org.reactivestreams.Subscriber

object Example11 extends App {

  Flowable.range(start = 1, count = 7)
    .lift(lifter = doOnEmpty(action = log(msg = s"Operation 1 Empty!")))
    .subscribe(n => log(msg = s"Operation 1: $n"))

  Flowable.empty[Int]
    .lift(lifter = doOnEmpty(action = log(msg = s"Operation 2 Empty!")))
    .subscribe(n => log(msg = s"Operation 2: $n"))

  def doOnEmpty[T](action: => Unit): FlowableOperator[T, T] = new DoOnEmptySubscriber[T](_, action)

  class DoOnEmptySubscriber[T](downstream: Subscriber[T], action: => Unit) extends DisposableSubscriber[T] {

    private var isEmpty = true

    override def onNext(t: T): Unit = {
      isEmpty = false
      downstream onNext t
    }

    override def onError(e: Throwable): Unit =
      downstream onError e

    override def onComplete(): Unit = {
      if (isEmpty) tryToExecute(action)(onError = downstream.onError)
      downstream.onComplete()
    }

  }

}

object Example12 extends App {

  Flowable.range(start = 1, count = 7)
    .lift(lifter = toList)
    .subscribe(l => log(msg = s"Operation 1: ${l.mkString("[", ", ", "]")}"))

  Flowable.empty[Int]
    .lift(lifter = toList)
    .subscribe(l => log(msg = s"Operation 2: ${l.mkString("[", ", ", "]")}"))

  def toList[T]: FlowableOperator[List[T], T] = new ToListSubscriber[T](_)

  class ToListSubscriber[T](downstream: Subscriber[List[T]]) extends DisposableSubscriber[T] {

    import scala.collection.mutable

    private val list = mutable.ArrayBuffer.empty[T]

    override def onNext(t: T): Unit =
      list += t

    override def onError(e: Throwable): Unit =
      downstream onError e

    override def onComplete(): Unit = {
      downstream onNext list.toList
      downstream.onComplete()
    }

  }

}
