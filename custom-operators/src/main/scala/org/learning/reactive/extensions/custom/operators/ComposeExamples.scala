package org.learning.reactive.extensions.custom.operators

import org.learning.reactive.extensions.core.Flowable.FlowableTransformer
import org.learning.reactive.extensions.core.Observable.ObservableTransformer
import org.learning.reactive.extensions.core.{Flowable, Observable, log}

import java.util.concurrent.atomic.AtomicInteger
import scala.collection.mutable.ArrayBuffer

object Example1 extends App {

  Observable.from(items = "Alpha", "Beta", "Gamma", "Delta", "Epsilon")
    .collect(initialItem = ArrayBuffer.empty[String])(_ += _)
    .map(_.toList)
    .subscribe(l => log(msg = s"${l mkString ", "}"))

  Observable.range(start = 1, count = 10)
    .collect(initialItem = ArrayBuffer.empty[Int])(_ += _)
    .map(_.toList)
    .subscribe(l => log(msg = s"${l mkString ", "}"))

}

object Example2 extends App {

  Observable.from(items = "Alpha", "Beta", "Gamma", "Delta", "Epsilon")
    .compose(toListTransformer)
    .subscribe(l => log(msg = s"${l mkString ", "}"))

  Observable.range(start = 1, count = 10)
    .compose(toListTransformer)
    .subscribe(l => log(msg = s"${l mkString ", "}"))

  def toListTransformer[T]: ObservableTransformer[T, List[T]] = _
    .collect(initialItem = ArrayBuffer.empty[T])(_ += _)
    .map(_.toList)
    .toObservable

}

object Example3 extends App {

  Observable.from(items = "Alpha", "Beta", "Gamma", "Delta", "Epsilon")
    .compose(joinToStringTransformer(separator = "/"))
    .subscribe(log _)

  def joinToStringTransformer(separator: String): ObservableTransformer[String, String] = upstream => {
    val stringBuilder: (StringBuilder, String) => Unit = {
      case (builder, str) =>
        if (builder.isEmpty) builder append str else builder append s"$separator$str"
    }

    upstream.collect(initialItem = new StringBuilder)(stringBuilder)
      .map(_.toString)
      .toObservable
  }

}

object Example4 extends App {

  Flowable.from(items = "Alpha", "Beta", "Gamma", "Delta", "Epsilon")
    .compose(toListTransformer)
    .subscribe(l => log(msg = s"${l mkString ", "}"))

  Flowable.range(start = 1, count = 12)
    .compose(toListTransformer)
    .subscribe(l => log(msg = s"${l mkString ", "}"))

  def toListTransformer[T]: FlowableTransformer[T, List[T]] = _
    .collect(initialItem = ArrayBuffer.empty[T])(_ += _)
    .map(_.toList)
    .toFlowable

}

object Example5 extends App {

  val indexedStrings = Observable.from(items = "Alpha", "Beta", "Gamma", "Delta", "Epsilon").compose(withIndex)

  // Observer 1
  indexedStrings.subscribe(str => log(msg = s"Subscriber 1: $str"))

  // Observer 2
  indexedStrings.subscribe(str => log(msg = s"Subscriber 2: $str"))

  def withIndex[T]: ObservableTransformer[T, IndexedValue[T]] = {
    val indexer = new AtomicInteger(-1)
    _.map(t => IndexedValue(indexer.incrementAndGet(), t))
  }

}

object Example6 extends App {

  val indexedStrings = Observable.from(items = "Alpha", "Beta", "Gamma", "Delta", "Epsilon").compose(withIndex)

  // Observer 1
  indexedStrings.subscribe(str => log(msg = s"Subscriber 1: $str"))

  // Observer 2
  indexedStrings.subscribe(str => log(msg = s"Subscriber 2: $str"))

  def withIndex[T]: ObservableTransformer[T, IndexedValue[T]] = upstream => {
    val indexer = new AtomicInteger(-1)
    upstream.map(t => IndexedValue(indexer.incrementAndGet(), t))
  }

}

object Example7 extends App {

  val indexedStrings = Observable.from(items = "Alpha", "Beta", "Gamma", "Delta", "Epsilon").compose(withIndex)

  // Observer 1
  indexedStrings.subscribe(str => log(msg = s"Subscriber 1: $str"))

  // Observer 2
  indexedStrings.subscribe(str => log(msg = s"Subscriber 2: $str"))

  def withIndex[T]: ObservableTransformer[T, IndexedValue[T]] = upstream => {
    Observable.defer {
      val indexer = new AtomicInteger(-1)
      upstream.map(t => IndexedValue(indexer.incrementAndGet(), t))
    }
  }

}

object Example8 extends App {

  val indexedStrings = Observable.from(items = "Alpha", "Beta", "Gamma", "Delta", "Epsilon").compose(withIndex)

  // Observer 1
  indexedStrings.subscribe(str => log(msg = s"Subscriber 1: $str"))

  // Observer 2
  indexedStrings.subscribe(str => log(msg = s"Subscriber 2: $str"))

  def withIndex[T]: ObservableTransformer[T, IndexedValue[T]] = upstream => {
    Observable.zip(source1 = upstream, source2 = Observable.range(start = 0, count = Int.MaxValue)) {
      case (value, index) =>
        IndexedValue(index, value)
    }
  }

}

case class IndexedValue[T](index: Int, value: T) {

  override def toString: String =
    s"$index - $value"

}
