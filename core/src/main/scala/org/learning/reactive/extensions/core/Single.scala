package org.learning.reactive.extensions.core

import io.reactivex.rxjava3.core.{Single => RxSingle}
import io.reactivex.rxjava3.functions.{Consumer, Function}

class Single[T](val rxSingle: RxSingle[T]) {

  def map[R](f: T => R): Single[R] = Single {
    val function: Function[T, R] = t => f(t)
    rxSingle map function
  }

  def subscribe[A >: T](onSuccess: A => Unit): Disposable = {
    val function1: Consumer[A] = a => onSuccess(a)

    Disposable {
      rxSingle subscribe function1
    }
  }

  def subscribe[A >: T](onSuccess: A => Unit, onError: Throwable => Unit): Disposable = {
    val function1: Consumer[A] = a => onSuccess(a)
    val function2: Consumer[Throwable] = t => onError(t)

    Disposable {
      rxSingle.subscribe(function1, function2)
    }
  }

}

object Single {

  def apply[T](rxSingle: RxSingle[T]): Single[T] =
    new Single(rxSingle)

  def just[T](item: T): Single[T] = Single {
    RxSingle just item
  }

}
