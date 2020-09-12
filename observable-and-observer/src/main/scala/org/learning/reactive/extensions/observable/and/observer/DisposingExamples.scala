package org.learning.reactive.extensions.observable.and.observer

import io.reactivex.rxjava3.observers.ResourceObserver
import org.learning.reactive.extensions.core._

import scala.annotation.tailrec
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}

object Example34 extends App {

  val seconds = Observable.interval(1.seconds)
  val disposable = seconds.subscribe(n => log(s"Received: $n"))

  sleep(7.seconds)

  disposable.dispose()

  sleep(7.seconds)

}

object Example35 extends App {

  val source = Observable.interval(1.seconds)

  val myObserver = new ResourceObserver[Long] {

    override def onNext(n: Long): Unit =
      log(n.toString)

    override def onError(e: Throwable): Unit =
      e.printStackTrace()

    override def onComplete(): Unit =
      log("Done!")

  }

  val disposable = Disposable(source subscribeWith myObserver)

  sleep(7.seconds)

  disposable.dispose()

  sleep(7.seconds)

}

object Example36 extends App {

  val disposables = CompositeDisposable()

  val source = Observable.interval(1.seconds)

  val disposable1 = source.subscribe(n => log(s"Observer 1: $n"))
  val disposable2 = source.subscribe(n => log(s"Observer 2: $n"))

  disposables.addAll(disposable1, disposable2)

  sleep(7.seconds)

  disposables.dispose()

  sleep(7.seconds)

}

object Example37 extends App {

  val source = Observable.create[Int] { emitter =>
    @tailrec
    def go(i: Int): Unit = i match {
      case 0 =>
        emitter.onComplete()
      case _ if emitter.isDisposed =>
        ()
      case _ =>
        emitter onNext i
        go(i = i - 1)
    }

    Try(go(1000)) match {
      case Success(_) =>
        ()
      case Failure(e) =>
        emitter onError e
    }
  }

  source.subscribe(n => log(s"$n"))

}
