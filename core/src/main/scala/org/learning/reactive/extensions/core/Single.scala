package org.learning.reactive.extensions.core

import io.reactivex.rxjava3.core.{SingleSource, Single => RxSingle, SingleTransformer => RxSingleTransformer}
import io.reactivex.rxjava3.functions.{Consumer, Function}
import org.learning.reactive.extensions.core.Single.SingleTransformer

class Single[T](val rxSingle: RxSingle[T]) {

  def blockingGet(): T = rxSingle.blockingGet()

  def compose[R](composer: SingleTransformer[T, R]): Single[R] = Single {
    val singleTransformer = new RxSingleTransformer[T, R] {
      override def apply(upstream: RxSingle[T]): SingleSource[R] = {
        val downstream = composer(Single(upstream))
        downstream.rxSingle
      }
    }

    rxSingle compose singleTransformer
  }

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

  def toFlowable: Flowable[T] = Flowable {
    rxSingle.toFlowable
  }

  def toObservable: Observable[T] = Observable {
    rxSingle.toObservable
  }

}

object Single {

  type SingleTransformer[U, D] = Single[U] => Single[D]

  def apply[T](rxSingle: RxSingle[T]): Single[T] =
    new Single(rxSingle)

  def just[T](item: T): Single[T] = Single {
    RxSingle just item
  }

}
