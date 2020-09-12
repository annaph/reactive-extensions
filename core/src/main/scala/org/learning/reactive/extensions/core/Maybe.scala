package org.learning.reactive.extensions.core

import io.reactivex.rxjava3.core.{Maybe => RxMaybe}
import io.reactivex.rxjava3.functions.{Action, Consumer}

class Maybe[T](val rxMaybe: RxMaybe[T]) {

  def doOnSuccess(onSuccess: T => Unit): Maybe[T] = Maybe {
    val consumer: Consumer[T] = t => onSuccess(t)
    rxMaybe doOnSuccess consumer
  }

  def subscribe(onSuccess: T => Unit): Disposable = {
    val consumer: Consumer[T] = t => onSuccess(t)

    Disposable {
      rxMaybe subscribe consumer
    }
  }

  def subscribe(onSuccess: T => Unit, onError: Throwable => Unit, onComplete: => Unit): Disposable = {
    val consumer1: Consumer[T] = t => onSuccess(t)
    val consumer2: Consumer[Throwable] = t => onError(t)
    val function3: Action = () => onComplete

    Disposable {
      rxMaybe.subscribe(consumer1, consumer2, function3)
    }
  }

}

object Maybe {

  def apply[T](rxMaybe: RxMaybe[T]): Maybe[T] =
    new Maybe(rxMaybe)

  def empty[T](): Maybe[T] = Maybe {
    RxMaybe.empty[T]()
  }

  def just[T](item: T): Maybe[T] = Maybe {
    RxMaybe just item
  }

}
