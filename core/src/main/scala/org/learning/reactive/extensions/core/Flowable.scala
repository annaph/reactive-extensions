package org.learning.reactive.extensions.core

import io.reactivex.rxjava3.core.{BackpressureOverflowStrategy, BackpressureStrategy, Emitter, FlowableEmitter, FlowableOnSubscribe, Scheduler, Flowable => RxFlowable}
import io.reactivex.rxjava3.functions.{Action, BiConsumer, Consumer, Function, Supplier}
import org.reactivestreams.Subscriber

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration

class Flowable[T](val rxFlowable: RxFlowable[T]) {

  def doOnNext(onNext: T => Unit): Flowable[T] = Flowable {
    val function: Consumer[T] = t => onNext(t)
    rxFlowable doOnNext function
  }

  def map[R](mapper: T => R): Flowable[R] = Flowable {
    val function: Function[T, R] = t => mapper(t)
    rxFlowable map function
  }

  def observeOn(scheduler: Scheduler): Flowable[T] = Flowable {
    rxFlowable observeOn scheduler
  }

  def onBackpressureBuffer(): Flowable[T] = Flowable {
    rxFlowable.onBackpressureBuffer()
  }

  def onBackpressureBuffer(capacity: Long,
                           onOverflow: => Unit,
                           overflowStrategy: BackpressureOverflowStrategy): Flowable[T] = Flowable {
    val action: Action = () => onOverflow
    rxFlowable.onBackpressureBuffer(capacity, action, overflowStrategy)
  }

  def onBackpressureDrop(onDrop: T => Unit): Flowable[T] = Flowable {
    val consumer: Consumer[T] = t => onDrop(t)
    rxFlowable onBackpressureDrop consumer
  }

  def onBackpressureLatest(): Flowable[T] = Flowable {
    rxFlowable.onBackpressureLatest()
  }

  def subscribeOn(scheduler: Scheduler): Flowable[T] = Flowable {
    rxFlowable subscribeOn scheduler
  }

  def subscribe[A >: T](onNext: A => Unit): Disposable = {
    val consumer: Consumer[A] = a => onNext(a)

    Disposable {
      rxFlowable subscribe consumer
    }
  }

  def subscribe[A >: T](onNext: A => Unit, onError: Throwable => Unit): Disposable = {
    val consumer1: Consumer[A] = a => onNext(a)
    val consumer2: Consumer[Throwable] = t => onError(t)

    Disposable {
      rxFlowable.subscribe(consumer1, consumer2)
    }
  }

  def subscribe[A >: T](onNext: A => Unit, onError: Throwable => Unit, onComplete: => Unit): Disposable = {
    val consumer1: Consumer[A] = a => onNext(a)
    val consumer2: Consumer[Throwable] = t => onError(t)
    val action: Action = () => onComplete

    Disposable {
      rxFlowable.subscribe(consumer1, consumer2, action)
    }
  }

  def subscribe[A >: T](subscriber: Subscriber[A]): Unit =
    rxFlowable subscribe subscriber

  def toObservable: Observable[T] = Observable {
    rxFlowable.toObservable
  }

}

object Flowable {

  def apply[T](rxFlowable: RxFlowable[T]): Flowable[T] =
    new Flowable(rxFlowable)

  def create[T](mode: BackpressureStrategy)(f: FlowableEmitter[T] => Unit): Flowable[T] = Flowable {
    val source = new FlowableOnSubscribe[T] {
      override def subscribe(emitter: FlowableEmitter[T]): Unit = f(emitter)
    }

    RxFlowable.create(source, mode)
  }

  def generate[T](generator: Emitter[T] => Unit): Flowable[T] = Flowable {
    val consumer: Consumer[Emitter[T]] = e => generator(e)
    RxFlowable generate consumer
  }

  def generate[S, T](initialState: => S)(generator: (S, Emitter[T]) => Unit): Flowable[T] = Flowable {
    val supplier: Supplier[S] = () => initialState
    val biConsumer: BiConsumer[S, Emitter[T]] = (s, t) => generator(s, t)

    RxFlowable.generate(supplier, biConsumer)
  }

  def interval(period: Duration): Flowable[Long] = Flowable {
    RxFlowable.interval(period.toMillis, TimeUnit.MILLISECONDS).map(_.longValue)
  }

  def range(start: Int, count: Int): Flowable[Int] = Flowable {
    RxFlowable.range(start, count).map(_.toInt)
  }

}
