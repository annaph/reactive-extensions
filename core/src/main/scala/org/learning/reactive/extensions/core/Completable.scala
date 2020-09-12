package org.learning.reactive.extensions.core

import io.reactivex.rxjava3.core.{Completable => RxCompletable}
import io.reactivex.rxjava3.functions.Action

class Completable(val rxCompletable: RxCompletable) {

  def subscribe(onComplete: => Unit): Disposable = {
    val function: Action = () => onComplete

    Disposable {
      rxCompletable subscribe function
    }
  }

}

object Completable {

  def apply(rxCompletable: RxCompletable): Completable =
    new Completable(rxCompletable)

  def fromRunnable(run: => Unit): Completable = Completable {
    val runnable: Runnable = () => run
    RxCompletable fromRunnable runnable
  }

}
