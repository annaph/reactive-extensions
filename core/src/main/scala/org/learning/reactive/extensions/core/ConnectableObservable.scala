package org.learning.reactive.extensions.core

import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.observables.{ConnectableObservable => RxConnectableObservable}

class ConnectableObservable[T](val rxConnectableObservable: RxConnectableObservable[T])
  extends Observable[T](rxConnectableObservable) {

  def connect(): Disposable =
    rxConnectableObservable.connect()

}

object ConnectableObservable {

  def apply[T](rxObservable: RxConnectableObservable[T]): ConnectableObservable[T] =
    new ConnectableObservable(rxObservable)

}
