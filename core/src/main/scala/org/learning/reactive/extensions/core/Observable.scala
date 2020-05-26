package org.learning.reactive.extensions.core

import java.util.concurrent.TimeUnit

import io.reactivex.rxjava3.core.{Observable => RxObservable}
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.{Consumer, Function}

import scala.concurrent.duration.Duration

class Observable[T](val rxObservable: RxObservable[T]) {

  def subscribe[A >: T](f: A => Unit): Disposable = {
    val function: Consumer[A] = a => f(a)
    rxObservable subscribe function
  }

  def map[R](f: T => R): Observable[R] = Observable {
    val function: Function[T, R] = t => f(t)
    rxObservable map function
  }

}

object Observable {

  def apply[T](rxObservable: RxObservable[T]): Observable[T] =
    new Observable(rxObservable)

  def just[T](item1: T, item2: T, item3: T): Observable[T] = Observable {
    RxObservable.just(item1, item2, item3)
  }

  def interval(period: Duration): Observable[Long] = Observable {
    RxObservable.interval(period.toMillis, TimeUnit.MILLISECONDS)
      .map(_.longValue)
  }

}
