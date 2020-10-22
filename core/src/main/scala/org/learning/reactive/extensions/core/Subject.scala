package org.learning.reactive.extensions.core

import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.{Disposable => RxDisposable}
import io.reactivex.rxjava3.subjects.{AsyncSubject, BehaviorSubject, PublishSubject, ReplaySubject, UnicastSubject, Subject => RxSubject}

class Subject[T](val rxSubject: RxSubject[T]) extends Observable[T](rxSubject) with Observer[T] {

  override def onSubscribe(rxDisposable: RxDisposable): Unit =
    rxSubject onSubscribe rxDisposable

  override def onNext(t: T): Unit =
    rxSubject onNext t

  override def onError(e: Throwable): Unit =
    rxSubject onError e

  override def onComplete(): Unit =
    rxSubject.onComplete()

  def onSubscribe(disposable: Disposable): Unit =
    rxSubject onSubscribe disposable.rxDisposable

  def toSerialized: Subject[T] = Subject {
    rxSubject.toSerialized
  }

}

object Subject {

  def apply[T](rxSubject: RxSubject[T]): Subject[T] =
    new Subject(rxSubject)

  def asyncSubject[T](): Subject[T] = Subject {
    AsyncSubject.create()
  }

  def behaviorSubject[T](): Subject[T] = Subject {
    BehaviorSubject.create()
  }

  def publishSubject[T](): Subject[T] = Subject {
    PublishSubject.create()
  }

  def replaySubject[T](): Subject[T] = Subject {
    ReplaySubject.create()
  }

  def unicastSubject[T](): Subject[T] = Subject {
    UnicastSubject.create()
  }

}
