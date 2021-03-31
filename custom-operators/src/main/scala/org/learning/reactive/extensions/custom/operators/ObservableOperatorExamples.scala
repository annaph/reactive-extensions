package org.learning.reactive.extensions.custom.operators

import io.reactivex.rxjava3.observers.DisposableObserver
import org.learning.reactive.extensions.core.Observable.ObservableOperator
import org.learning.reactive.extensions.core.{Observable, log, tryToExecute}

object Example9 extends App {

  Observable.range(start = 1, count = 7)
    .lift(lifter = doOnEmpty(action = log(msg = s"Operation 1 Empty!")))
    .subscribe(n => log(msg = s"Operation 1: $n"))

  Observable.empty[Int]
    .lift(lifter = doOnEmpty(action = log(msg = s"Operation 2 Empty!")))
    .subscribe(n => log(msg = s"Operation 2: $n"))

  def doOnEmpty[T](action: => Unit): ObservableOperator[T, T] = downstream => new DisposableObserver[T] {

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

object Example10 extends App {

  Observable.range(start = 1, count = 7)
    .lift(lifter = toList)
    .subscribe(l => log(msg = s"Operation 1: ${l.mkString("[", ", ", "]")}"))

  Observable.empty[Int]
    .lift(lifter = toList)
    .subscribe(l => log(msg = s"Operation 2: ${l.mkString("[", ", ", "]")}"))

  def toList[T]: ObservableOperator[List[T], T] = downstream => new DisposableObserver[T] {

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
