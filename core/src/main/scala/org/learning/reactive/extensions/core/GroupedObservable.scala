package org.learning.reactive.extensions.core

import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.observables.{GroupedObservable => RxGroupedObservable}

import scala.jdk.CollectionConverters._

class GroupedObservable[K, T](val rxGroupedObservable: RxGroupedObservable[K, T]) {

  def getKey: K = rxGroupedObservable.getKey()

  def observeOn(scheduler: Scheduler): Observable[T] = Observable {
    rxGroupedObservable observeOn scheduler
  }

  def toList(): Single[List[T]] = Single {
    rxGroupedObservable.toList().map(_.asScala).map(_.toList)
  }

}

object GroupedObservable {

  def apply[K, T](rxGroupedObservable: RxGroupedObservable[K, T]): GroupedObservable[K, T] =
    new GroupedObservable(rxGroupedObservable)

}
