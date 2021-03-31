package org.learning.reactive.extensions.core

import io.reactivex.rxjava3.core.{BackpressureOverflowStrategy, BackpressureStrategy, Emitter, FlowableEmitter, FlowableOnSubscribe, Scheduler, Flowable => RxFlowable, FlowableOperator => RxFlowableOperator, FlowableTransformer => RxFlowableTransformer}
import io.reactivex.rxjava3.functions.{Action, BiConsumer, Consumer, Function, Supplier}
import org.learning.reactive.extensions.core.Flowable.{FlowableOperator, FlowableTransformer}
import org.reactivestreams.{Publisher, Subscriber}

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration
import scala.jdk.CollectionConverters._

class Flowable[T](val rxFlowable: RxFlowable[T]) {

  def collect[U](initialItem: U)(f: => (U, T) => Unit): Single[U] = Single {
    val supplier: Supplier[U] = () => initialItem
    val biConsumer: BiConsumer[U, T] = (u, t) => f(u, t)

    rxFlowable.collect(supplier, biConsumer)
  }

  def compose[R](composer: FlowableTransformer[T, R]): Flowable[R] = Flowable {
    val flowableTransformer = new RxFlowableTransformer[T, R] {
      override def apply(upstream: RxFlowable[T]): Publisher[R] = {
        val downstream = composer.apply(Flowable(upstream))
        downstream.rxFlowable
      }
    }

    rxFlowable compose flowableTransformer
  }

  def doOnNext(onNext: T => Unit): Flowable[T] = Flowable {
    val function: Consumer[T] = t => onNext(t)
    rxFlowable doOnNext function
  }

  def lift[R](lifter: FlowableOperator[R, T]): Flowable[R] = Flowable {
    val flowableOperator = new RxFlowableOperator[R, T] {
      override def apply(subscriber: Subscriber[_ >: R]): Subscriber[_ >: T] =
        lifter.apply(subscriber.asInstanceOf[Subscriber[R]])
    }

    rxFlowable lift flowableOperator
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

  def range(start: Int, count: Int): Flowable[Int] = Flowable {
    RxFlowable.range(start, count).map(_.toInt)
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

  type FlowableTransformer[U, D] = Flowable[U] => Flowable[D]

  type FlowableOperator[D, U] = Subscriber[D] => Subscriber[U]

  def apply[T](rxFlowable: RxFlowable[T]): Flowable[T] =
    new Flowable(rxFlowable)

  def create[T](mode: BackpressureStrategy)(f: FlowableEmitter[T] => Unit): Flowable[T] = Flowable {
    val source = new FlowableOnSubscribe[T] {
      override def subscribe(emitter: FlowableEmitter[T]): Unit = f(emitter)
    }

    RxFlowable.create(source, mode)
  }

  def empty[T]: Flowable[T] = Flowable {
    RxFlowable.empty()
  }

  def from[T](items: T*): Flowable[T] = Flowable {
    RxFlowable fromIterable items.asJava
  }

  def from[T](iterable: Iterable[T]): Flowable[T] = Flowable {
    RxFlowable fromIterable iterable.asJava
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
