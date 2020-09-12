package org.learning.reactive.extensions.core

import io.reactivex.rxjava3.disposables.{CompositeDisposable => RxCompositeDisposable}

class CompositeDisposable(val rxCompositeDisposable: RxCompositeDisposable) {

  def addAll(disposables: Disposable*): Boolean =
    disposables
      .map(_.rxDisposable)
      .map(rxCompositeDisposable.add)
      .forall(success => success)

  def dispose(): Unit =
    rxCompositeDisposable.dispose()

}

object CompositeDisposable {

  def apply(): CompositeDisposable =
    new CompositeDisposable(new RxCompositeDisposable())

}
