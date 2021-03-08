package org.learning.reactive.extensions.core

import io.reactivex.rxjava3.core.{BackpressureStrategy, Notification, ObservableEmitter, ObservableOnSubscribe, Observer, Scheduler, Observable => RxObservable, Single => RxSingle}
import io.reactivex.rxjava3.disposables.{Disposable => RxDisposable}
import io.reactivex.rxjava3.functions.{Action, BiConsumer, BiFunction, Consumer, Function, Predicate, Supplier}
import io.reactivex.rxjava3.schedulers.Timed

import java.util.Comparator
import java.util.concurrent.{Callable, CompletableFuture, TimeUnit}
import scala.concurrent.duration.Duration
import scala.concurrent.{ExecutionContext, Future}
import scala.jdk.CollectionConverters._
import scala.util.{Failure, Success}

class Observable[T](val rxObservable: RxObservable[T]) {

  def ambWith(other: Observable[T]): Observable[T] = Observable {
    rxObservable ambWith other.rxObservable
  }

  def blockingSubscribe[A >: T](onNext: A => Unit, onError: Throwable => Unit, onComplete: => Unit): Unit = {
    val consumer1: Consumer[A] = a => onNext(a)
    val consumer2: Consumer[Throwable] = t => onError(t)
    val action: Action = () => onComplete

    rxObservable.blockingSubscribe(consumer1, consumer2, action)
  }

  def buffer(count: Int): Observable[List[T]] = Observable {
    rxObservable.buffer(count).map(_.asScala).map(_.toList)
  }

  def buffer(count: Int, skip: Int): Observable[List[T]] = Observable {
    rxObservable.buffer(count, skip).map(_.asScala).map(_.toList)
  }

  def buffer[U <: java.util.Collection[T]](count: Int, bufferSupplier: => U): Observable[U] = Observable {
    val supplier: Supplier[U] = () => bufferSupplier
    rxObservable.buffer(count, supplier)
  }

  def buffer(timespan: Duration): Observable[List[T]] = Observable {
    rxObservable.buffer(timespan.toMillis, TimeUnit.MILLISECONDS).map(_.asScala).map(_.toList)
  }

  def buffer(timespan: Duration, timeskip: Duration): Observable[List[T]] = Observable {
    rxObservable.buffer(timespan.toMillis, timeskip.toMillis, TimeUnit.MILLISECONDS).map(_.asScala).map(_.toList)
  }

  def buffer(timespan: Duration, count: Int): Observable[List[T]] = Observable {
    rxObservable.buffer(timespan.toMillis, TimeUnit.MILLISECONDS, count).map(_.asScala).map(_.toList)
  }

  def buffer[B](boundary: Observable[B]): Observable[List[T]] = Observable {
    rxObservable.buffer(boundary.rxObservable).map(_.asScala).map(_.toList)
  }

  def cache(): Observable[T] = Observable {
    rxObservable.cache()
  }

  def cacheWithInitialCapacity(initialCapacity: Int): Observable[T] = Observable {
    rxObservable cacheWithInitialCapacity initialCapacity
  }

  def collect[U](initialItem: U)(f: => (U, T) => Unit): Single[U] = Single {
    val supplier: Supplier[U] = () => initialItem
    val biConsumer: BiConsumer[U, T] = (u, t) => f(u, t)

    rxObservable.collect(supplier, biConsumer)
  }

  def concatMap[R](mapper: T => Observable[R]): Observable[R] = Observable {
    val function: Function[T, RxObservable[R]] = t => mapper(t).rxObservable
    rxObservable concatMap function
  }

  def concatWith(other: Observable[T]): Observable[T] = Observable {
    rxObservable concatWith other.rxObservable
  }

  def contains(item: T): Single[Boolean] = Single {
    rxObservable.contains(item).map(_.booleanValue)
  }

  def count(): Single[Long] = Single {
    rxObservable.count().map(_.toLong)
  }

  def defaultIfEmpty(default: T): Observable[T] = Observable {
    rxObservable defaultIfEmpty default
  }

  def delay(period: Duration): Observable[T] = Observable {
    rxObservable.delay(period.toMillis, TimeUnit.MILLISECONDS)
  }

  def distinct(): Observable[T] = Observable {
    rxObservable.distinct()
  }

  def distinct[K](keySelector: T => K): Observable[T] = Observable {
    val function: Function[T, K] = t => keySelector(t)
    rxObservable distinct function
  }

  def distinctUntilChanged(): Observable[T] = Observable {
    rxObservable.distinctUntilChanged()
  }

  def distinctUntilChanged[K](keySelector: T => K): Observable[T] = Observable {
    val function: Function[T, K] = t => keySelector(t)
    rxObservable distinctUntilChanged function
  }

  def doAfterNext(onAfterNext: T => Unit): Observable[T] = Observable {
    val consumer: Consumer[T] = t => onAfterNext(t)
    rxObservable doAfterNext consumer
  }

  def doFinally(onFinally: => Unit): Observable[T] = Observable {
    val action: Action = () => onFinally
    rxObservable doFinally action
  }

  def doOnComplete(onComplete: => Unit): Observable[T] = Observable {
    val action: Action = () => onComplete
    rxObservable doOnComplete action
  }

  def doAfterTerminate(onAfterTerminate: => Unit): Observable[T] = Observable {
    val action: Action = () => onAfterTerminate
    rxObservable doAfterTerminate action
  }

  def doOnEach(onNotification: Notification[T] => Unit): Observable[T] = Observable {
    val function: Consumer[Notification[T]] = notification => onNotification(notification)
    rxObservable doOnEach function
  }

  def doOnError(onError: Throwable => Unit): Observable[T] = Observable {
    val function: Consumer[Throwable] = e => onError(e)
    rxObservable doOnError function
  }

  def doOnDispose(onDispose: => Unit): Observable[T] = Observable {
    val action: Action = () => onDispose
    rxObservable doOnDispose action
  }

  def doOnNext(onNext: T => Unit): Observable[T] = Observable {
    val function: Consumer[T] = t => onNext(t)
    rxObservable doOnNext function
  }

  def doOnSubscribe(onSubscribe: Disposable => Unit): Observable[T] = Observable {
    val function: Consumer[RxDisposable] = d => onSubscribe(Disposable(d))
    rxObservable doOnSubscribe function
  }

  def drop(count: Long): Observable[T] = Observable {
    rxObservable skip count
  }

  def dropWhile(p: T => Boolean): Observable[T] = Observable {
    val predicate: Predicate[T] = t => p(t)
    rxObservable skipWhile predicate
  }

  def elementAt(index: Long): Maybe[T] = Maybe {
    rxObservable elementAt index
  }

  def exists(p: T => Boolean): Single[Boolean] = Single {
    val predicate: Predicate[T] = t => p(t)
    rxObservable.any(predicate).map(_.booleanValue)
  }

  def filter(predicate: T => Boolean): Observable[T] = Observable {
    val function: Predicate[T] = t => predicate(t)
    rxObservable filter function
  }

  def first(default: T): Single[T] = Single {
    rxObservable first default
  }

  def firstElement(): Maybe[T] = Maybe {
    rxObservable.firstElement()
  }

  def flatMap[R](mapper: T => Observable[R]): Observable[R] = Observable {
    val function: Function[T, RxObservable[R]] = t => mapper(t).rxObservable
    rxObservable flatMap function
  }

  def flatMap[U, R](mapper: T => Observable[U], combiner: (T, U) => R): Observable[R] = Observable {
    val function: Function[T, RxObservable[U]] = t => mapper(t).rxObservable
    val biFunction: BiFunction[T, U, R] = (t, u) => combiner(t, u)

    rxObservable.flatMap(function, biFunction)
  }

  def flatMapSingle[R](mapper: T => Single[R]): Observable[R] = Observable {
    val function: Function[T, RxSingle[R]] = t => mapper(t).rxSingle
    rxObservable.flatMapSingle(function)
  }

  def fold(op: (T, T) => T): Maybe[T] = Maybe {
    val biFunction: BiFunction[T, T, T] = (acc, t) => op(acc, t)
    rxObservable reduce biFunction
  }

  def foldLeft[U](z: U)(op: (U, T) => U): Single[U] = Single {
    val biFunction: BiFunction[U, T, U] = (acc, t) => op(acc, t)
    rxObservable.reduce(z, biFunction)
  }

  def forall(p: T => Boolean): Single[Boolean] = Single {
    val predicate: Predicate[T] = t => p(t)
    rxObservable.all(predicate).map(_.booleanValue)
  }

  def groupBy[K](keySelector: T => K): Observable[GroupedObservable[K, T]] = Observable {
    val function: Function[T, K] = t => keySelector(t)
    rxObservable.groupBy(function).map(GroupedObservable(_))
  }

  def isEmpty(): Single[Boolean] = Single {
    rxObservable.isEmpty().map(_.booleanValue)
  }

  def map[R](mapper: T => R): Observable[R] = Observable {
    val function: Function[T, R] = t => mapper(t)
    rxObservable map function
  }

  def mergeWith(other: Observable[T]): Observable[T] = Observable {
    rxObservable mergeWith other.rxObservable
  }

  def observeOn(scheduler: Scheduler): Observable[T] = Observable {
    rxObservable observeOn scheduler
  }

  def onErrorResumeNext(fallback: Throwable => Observable[T]): Observable[T] = Observable {
    val function: Function[Throwable, RxObservable[T]] = e => fallback(e).rxObservable
    rxObservable onErrorResumeNext function
  }

  def onErrorResumeWith(fallback: Observable[T]): Observable[T] = Observable {
    rxObservable onErrorResumeWith fallback.rxObservable
  }

  def onErrorReturn(item: Throwable => T): Observable[T] = Observable {
    val function: Function[Throwable, T] = e => item(e)
    rxObservable onErrorReturn function
  }

  def onErrorReturnItem(item: T): Observable[T] = Observable {
    rxObservable onErrorReturnItem item
  }

  def publish(): ConnectableObservable[T] = ConnectableObservable {
    rxObservable.publish()
  }

  def repeat(): Observable[T] = Observable {
    rxObservable.repeat()
  }

  def repeat(times: Long): Observable[T] = Observable {
    rxObservable repeat times
  }

  def replay(): ConnectableObservable[T] = ConnectableObservable {
    rxObservable.replay()
  }

  def replay(bufferSize: Int): ConnectableObservable[T] = ConnectableObservable {
    rxObservable replay bufferSize
  }

  def replay(window: Duration): ConnectableObservable[T] = ConnectableObservable {
    rxObservable.replay(window.toMillis, TimeUnit.MILLISECONDS)
  }

  def replay(bufferSize: Int, window: Duration): ConnectableObservable[T] = ConnectableObservable {
    rxObservable.replay(bufferSize, window.toMillis, TimeUnit.MILLISECONDS)
  }

  def retry(): Observable[T] = Observable {
    rxObservable.retry()
  }

  def retry(times: Long): Observable[T] = Observable {
    rxObservable retry times
  }

  def single(defaultItem: T): Single[T] = Single {
    rxObservable single defaultItem
  }

  def scan(op: (T, T) => T): Observable[T] = Observable {
    val biFunction: BiFunction[T, T, T] = (acc, t) => op(acc, t)
    rxObservable scan biFunction
  }

  def scanLeft[U](z: U)(op: (U, T) => U): Observable[U] = Observable {
    val bifunction: BiFunction[U, T, U] = (acc, t) => op(acc, t)
    rxObservable.scan(z, bifunction)
  }

  def share(): Observable[T] = Observable {
    rxObservable.share()
  }

  def sorted(implicit ord: Ordering[T]): Observable[T] = Observable {
    rxObservable sorted ord
  }

  def sorted[U](keyExtractor: T => U)(implicit ord: Ordering[U]): Observable[T] = Observable {
    val function: java.util.function.Function[T, U] = t => keyExtractor(t)
    val comparator: Comparator[U] = (u1, u2) => ord.compare(u1, u2)

    rxObservable sorted Comparator.comparing(function, comparator)
  }

  def startWith(items: T*): Observable[T] = Observable {
    rxObservable.startWithArray(items: _*)
  }

  def startWith(items: Iterable[T]): Observable[T] = Observable {
    rxObservable startWithIterable items.asJava
  }

  def startWithItem(item: T): Observable[T] = Observable {
    rxObservable startWithItem item
  }

  def subscribe[A >: T](onNext: A => Unit): Disposable = {
    val consumer: Consumer[A] = a => onNext(a)

    Disposable {
      rxObservable subscribe consumer
    }
  }

  def subscribe[A >: T](onNext: A => Unit, onError: Throwable => Unit): Disposable = {
    val consumer1: Consumer[A] = a => onNext(a)
    val consumer2: Consumer[Throwable] = t => onError(t)

    Disposable {
      rxObservable.subscribe(consumer1, consumer2)
    }
  }

  def subscribe[A >: T](onNext: A => Unit, onError: Throwable => Unit, onComplete: => Unit): Disposable = {
    val consumer1: Consumer[A] = a => onNext(a)
    val consumer2: Consumer[Throwable] = t => onError(t)
    val action: Action = () => onComplete

    Disposable {
      rxObservable.subscribe(consumer1, consumer2, action)
    }
  }

  def subscribe[A >: T](observer: Observer[A]): Unit =
    rxObservable subscribe observer

  def subscribeOn(scheduler: Scheduler): Observable[T] = Observable {
    rxObservable subscribeOn scheduler
  }

  def subscribeWith[A >: T, E <: Observer[A]](observer: E): E =
    rxObservable subscribeWith observer

  def switchIfEmpty(other: Observable[T]): Observable[T] = Observable {
    rxObservable switchIfEmpty other.rxObservable
  }

  def switchMap[R](mapper: T => Observable[R]): Observable[R] = Observable {
    val function: Function[T, RxObservable[R]] = t => mapper(t).rxObservable
    rxObservable switchMap function
  }

  def take(count: Long): Observable[T] = Observable {
    rxObservable take count
  }

  def take(period: Duration): Observable[T] = Observable {
    rxObservable.take(period.toMillis, TimeUnit.MILLISECONDS)
  }

  def takeWhile(p: T => Boolean): Observable[T] = Observable {
    val predicate: Predicate[T] = t => p(t)
    rxObservable takeWhile predicate
  }

  def throttleFirst(interval: Duration): Observable[T] = Observable {
    rxObservable.throttleFirst(interval.toMillis, TimeUnit.MILLISECONDS)
  }

  def throttleLast(interval: Duration): Observable[T] = Observable {
    rxObservable.throttleLast(interval.toMillis, TimeUnit.MILLISECONDS)
  }

  def throttleWithTimeout(timeout: Duration): Observable[T] = Observable {
    rxObservable.throttleWithTimeout(timeout.toMillis, TimeUnit.MILLISECONDS)
  }

  def timeInterval(unit: TimeUnit): Observable[Timed[T]] = Observable {
    rxObservable timeInterval unit
  }

  def timestamp(unit: TimeUnit): Observable[Timed[T]] = Observable {
    rxObservable timestamp unit
  }

  def toFlowable(strategy: BackpressureStrategy): Flowable[T] = Flowable {
    rxObservable.toFlowable(strategy)
  }

  def toList(): Single[List[T]] = Single {
    rxObservable.toList().map(_.asScala).map(_.toList)
  }

  def toList(capacityHint: Int): Single[List[T]] = Single {
    rxObservable.toList(capacityHint).map(_.asScala).map(_.toList)
  }

  def toListJava(collectionSupplier: => java.util.Collection[T]): Single[java.util.Collection[T]] = Single {
    val function: Supplier[java.util.Collection[T]] = () => collectionSupplier
    rxObservable.toList(function)
  }

  def toMap[K](keySelector: T => K): Single[Map[K, T]] = Single {
    val function: Function[T, K] = t => keySelector(t)
    rxObservable.toMap(function).map(_.asScala).map(_.toMap)
  }

  def toMap[K, V](keySelector: T => K)(valueSelector: T => V): Single[Map[K, V]] = Single {
    val function1: Function[T, K] = t => keySelector(t)
    val function2: Function[T, V] = t => valueSelector(t)

    rxObservable.toMap(function1, function2).map(_.asScala).map(_.toMap)
  }

  def toMapJava[K, V](mapSupplier: => java.util.Map[K, V])
                     (keySelector: T => K)(valueSelector: T => V): Single[java.util.Map[K, V]] = Single {
    val function1: Function[T, K] = t => keySelector(t)
    val function2: Function[T, V] = t => valueSelector(t)
    val supplier: Supplier[java.util.Map[K, V]] = () => mapSupplier

    rxObservable.toMap(function1, function2, supplier)
  }

  def toMultimap[K](keySelector: T => K): Single[Map[K, Seq[T]]] = Single {
    val function: Function[T, K] = t => keySelector(t)

    val mapper: Map[K, java.util.Collection[T]] => Map[K, Seq[T]] = m => m.map {
      case (key, value) =>
        key -> value.asScala.toSeq
    }

    rxObservable.toMultimap(function).map(_.asScala.toMap).map(mapper(_))
  }

  def toSortedList()(implicit ord: Ordering[T]): Single[List[T]] = Single {
    rxObservable.toSortedList(ord).map(_.asScala).map(_.toList)
  }

  def unsubscribeOn(scheduler: Scheduler): Observable[T] = Observable {
    rxObservable unsubscribeOn scheduler
  }

  def zipWith[U, R](other: Observable[U])(zipper: (T, U) => R): Observable[R] = Observable {
    val biFunction: BiFunction[T, U, R] = (t, u) => zipper(t, u)
    rxObservable.zipWith(other.rxObservable, biFunction)
  }

  def window(count: Int): Observable[Observable[T]] = Observable {
    rxObservable.window(count).map(Observable(_))
  }

  def window(count: Int, skip: Int): Observable[Observable[T]] = Observable {
    rxObservable.window(count, skip).map(Observable(_))
  }

  def window(timespan: Duration): Observable[Observable[T]] = Observable {
    rxObservable.window(timespan.toMillis, TimeUnit.MILLISECONDS).map(Observable(_))
  }

  def window[B](boundary: Observable[B]): Observable[Observable[T]] = Observable {
    rxObservable.window(boundary.rxObservable).map(Observable(_))
  }

  def withLatestFrom[U, R](other: Observable[U])(combiner: (T, U) => R): Observable[R] = Observable {
    val biFunction: BiFunction[T, U, R] = (t, u) => combiner(t, u)
    rxObservable.withLatestFrom(other.rxObservable, biFunction)
  }

}

object Observable {

  def apply[T](rxObservable: RxObservable[T]): Observable[T] =
    new Observable(rxObservable)

  def amb[T](sources: Observable[T]*): Observable[T] = Observable {
    RxObservable amb sources.map(_.rxObservable).asJava
  }

  def amb[T](sources: Iterable[Observable[T]]): Observable[T] = Observable {
    RxObservable amb sources.map(_.rxObservable).asJava
  }

  def combineLatest[T1, T2, R](source1: Observable[T1], source2: Observable[T2])
                              (combiner: (T1, T2) => R): Observable[R] = Observable {
    val biFunction: BiFunction[T1, T2, R] = (t1, t2) => combiner(t1, t2)
    RxObservable.combineLatest(source1.rxObservable, source2.rxObservable, biFunction)
  }

  def concat[T](sources: Observable[T]*): Observable[T] = Observable {
    RxObservable concat sources.map(_.rxObservable).asJava
  }

  def concat[T](sources: Iterable[Observable[T]]): Observable[T] = Observable {
    RxObservable concat sources.map(_.rxObservable).asJava
  }

  def create[T](f: ObservableEmitter[T] => Unit): Observable[T] = Observable {
    val source = new ObservableOnSubscribe[T] {
      override def subscribe(emitter: ObservableEmitter[T]): Unit = f(emitter)
    }

    RxObservable create source
  }

  def empty[T](): Observable[T] = Observable {
    RxObservable.empty()
  }

  def error[T](throwable: Throwable): Observable[T] = Observable {
    RxObservable error throwable
  }

  def error[T](throwable: () => Throwable): Observable[T] = Observable {
    val supplier = new Supplier[Throwable] {
      override def get(): Throwable = throwable()
    }

    RxObservable error supplier
  }

  def defer[T](observable: => Observable[T]): Observable[T] = Observable {
    val supplier: Supplier[RxObservable[T]] = () => observable.rxObservable
    RxObservable defer supplier
  }

  def from[T](items: T*): Observable[T] = Observable {
    RxObservable fromIterable items.asJava
  }

  def from[T](iterable: Iterable[T]): Observable[T] = Observable {
    RxObservable fromIterable iterable.asJava
  }

  def from[T](future: Future[T])(implicit ex: ExecutionContext): Observable[T] = Observable {
    val javaFuture = new CompletableFuture[T]()

    future.onComplete {
      case Success(value) =>
        javaFuture complete value
      case Failure(e) =>
        javaFuture completeExceptionally e
    }

    RxObservable fromFuture javaFuture
  }

  def fromAction[T](action: => Unit): Observable[T] = Observable {
    val function: Action = () => action
    RxObservable fromAction function
  }

  def fromCallable[T](callable: => T): Observable[T] = Observable {
    val function: Callable[T] = () => callable
    RxObservable fromCallable function
  }

  def interval(period: Duration): Observable[Long] = Observable {
    RxObservable.interval(period.toMillis, TimeUnit.MILLISECONDS).map(_.longValue)
  }

  def interval(period: Duration, scheduler: Scheduler): Observable[Long] = Observable {
    RxObservable.interval(period.toMillis, TimeUnit.MILLISECONDS, scheduler).map(_.longValue)
  }

  def just[T](item: T): Observable[T] = Observable {
    RxObservable just item
  }

  def just[T](item1: T, item2: T): Observable[T] = Observable {
    RxObservable.just(item1, item2)
  }

  def just[T](item1: T, item2: T, item3: T): Observable[T] = Observable {
    RxObservable.just(item1, item2, item3)
  }

  def never[T](): Observable[T] = Observable {
    RxObservable.never()
  }

  def merge[T](sources: Observable[T]*): Observable[T] = Observable {
    RxObservable merge sources.map(_.rxObservable).asJava
  }

  def merge[T](sources: Iterable[Observable[T]]): Observable[T] = Observable {
    RxObservable merge sources.map(_.rxObservable).asJava
  }

  def range(start: Int, count: Int): Observable[Int] = Observable {
    RxObservable.range(start, count).map(_.toInt)
  }

  def sequenceEqual[T](source1: Observable[T], source2: Observable[T]): Single[Boolean] = Single {
    RxObservable.sequenceEqual(source1.rxObservable, source2.rxObservable).map(_.booleanValue)
  }

  def zip[T1, T2, R](source1: Observable[T1], source2: Observable[T2])(zipper: (T1, T2) => R): Observable[R] =
    Observable {
      val biFunction: BiFunction[T1, T2, R] = (t1, t2) => zipper(t1, t2)
      RxObservable.zip(source1.rxObservable, source2.rxObservable, biFunction)
    }

}
