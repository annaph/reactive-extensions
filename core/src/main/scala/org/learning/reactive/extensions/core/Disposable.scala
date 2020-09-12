package org.learning.reactive.extensions.core

import io.reactivex.rxjava3.disposables.{Disposable => RxDisposable}

class Disposable(val rxDisposable: RxDisposable) {

  def dispose(): Unit =
    rxDisposable.dispose()

  def isDisposed: Boolean =
    rxDisposable.isDisposed

}

object Disposable {

  def apply(rxDisposable: RxDisposable): Disposable =
    new Disposable(rxDisposable)

}
