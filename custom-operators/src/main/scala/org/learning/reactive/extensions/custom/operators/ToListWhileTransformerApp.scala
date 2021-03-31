package org.learning.reactive.extensions.custom.operators

import io.reactivex.rxjava3.core.{BackpressureStrategy, FlowableEmitter}
import org.learning.reactive.extensions.core.Flowable.FlowableTransformer
import org.learning.reactive.extensions.core.{Flowable, log}

import java.util.concurrent.atomic.AtomicReference
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object ToListWhileTransformerApp extends App {

  val items = List("Alpha", "Beta", "Zeta", "Gamma", "Delta", "Theta", "Epsilon")

  val listPredicate1: (List[String], String) => Boolean = {
    case (Nil, _) =>
      true
    case (x :: _, str) =>
      x.length == str.length
  }

  val listPredicate2: (List[String], String) => Boolean = {
    case (Nil, _) =>
      true
    case (xs, str) =>
      str > xs.last
  }

  log(msg = "=== Flowable 1 ===>")
  Flowable.from(items)
    .compose(composer = toListWhile(listPredicate1))
    .subscribe(l => log(msg = s"${l.mkString("[", ", ", "]")}"))

  log(msg = "=== Flowable 2 ===>")
  Flowable.from(items)
    .compose(composer = toListWhile(listPredicate2))
    .subscribe(l => log(msg = s"${l.mkString("[", ", ", "]")}"))

  def toListWhile[T](predicate: (List[T], T) => Boolean): FlowableTransformer[T, List[T]] = upstream => {
    Flowable.create(BackpressureStrategy.MISSING)(createSource(upstream)(predicate))
  }

  def createSource[T](upstream: Flowable[T])
                     (predicate: (List[T], T) => Boolean): FlowableEmitter[List[T]] => Unit = { emitter =>
    val cache = new AtomicReference[mutable.ListBuffer[T]](ListBuffer.empty[T])

    val onNext: T => Unit = { t =>
      val currCache = cache.get()
      if (predicate(currCache.toList, t)) cache.set(currCache += t) else {
        val list = cache.get().toList
        cache set ListBuffer(t)
        emitter onNext list
      }
    }

    val onError: Throwable => Unit = emitter.onError

    lazy val onComplete: Unit = {
      if (cache.get().nonEmpty) emitter onNext cache.get().toList
      emitter.onComplete()
    }

    upstream.subscribe(onNext, onError, onComplete)
  }

}
