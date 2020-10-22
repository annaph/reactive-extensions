package org.learning.reactive.extensions.core

import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.observables.{ConnectableObservable => RxConnectableObservable}

class ConnectableObservable[T](val rxConnectableObservable: RxConnectableObservable[T])
  extends Observable[T](rxConnectableObservable) {

  def autoConnect(): Observable[T] = Observable {
    rxConnectableObservable.autoConnect()
  }

  def autoConnect(numberOfSubscribers: Int): Observable[T] = Observable {
    rxConnectableObservable autoConnect numberOfSubscribers
  }

  def connect(): Disposable =
    rxConnectableObservable.connect()

  def refCount(): Observable[T] = Observable {
    rxConnectableObservable.refCount()
  }

}

object ConnectableObservable {

  def apply[T](rxObservable: RxConnectableObservable[T]): ConnectableObservable[T] =
    new ConnectableObservable(rxObservable)

}
