package org.learning.reactive.extensions.backpressure

import io.reactivex.rxjava3.core.{BackpressureStrategy, FlowableEmitter}
import io.reactivex.rxjava3.schedulers.Schedulers
import org.learning.reactive.extensions.core._

import scala.annotation.tailrec
import scala.concurrent.duration._

object Example7 extends App {

  val source = Observable.create[Int] { emitter =>
    @tailrec
    def emit(i: Int = 0): Unit = i match {
      case 1001 =>
        emitter.onComplete()
        ()
      case _ if !emitter.isDisposed =>
        emitter onNext i
        emit(i + 1)
      case _ =>
        ()
    }

    emit()
  }

  source
    .observeOn(Schedulers.io())
    .subscribe(n => log(msg = s"$n"))

  sleep(7.seconds)

}

object Example8 extends App {

  val source: FlowableEmitter[Int] => Unit = emitter => {
    @tailrec
    def emit(i: Int = 0): Unit = i match {
      case 1001 =>
        emitter.onComplete()
        ()
      case _ if !emitter.isCancelled =>
        emitter onNext i
        emit(i + 1)
      case _ =>
        ()
    }

    emit()
  }

  Flowable.create(BackpressureStrategy.BUFFER)(source)
    .observeOn(Schedulers.io())
    .subscribe(n => log(msg = s"$n"))

  sleep(7.seconds)

}

object Example9 extends App {

  Observable.range(start = 1, count = 1000)
    .toFlowable(BackpressureStrategy.BUFFER)
    .observeOn(Schedulers.io())
    .subscribe(n => log(msg = s"$n"))

  sleep(7.seconds)

}

object Example10 extends App {

  val numbers = Flowable.range(start = 0, count = 12)

  Observable.from(items = "Alpha", "Beta", "Gamma", "Delta", "Epsilon")
    .observeOn(Schedulers.computation())
    .flatMap(str => numbers.map(n => s"$n - $str").toObservable)
    .subscribe(log _)

  sleep(12.seconds)

}
