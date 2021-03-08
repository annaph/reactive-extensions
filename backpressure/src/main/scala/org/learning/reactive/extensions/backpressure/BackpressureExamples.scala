package org.learning.reactive.extensions.backpressure

import io.reactivex.rxjava3.schedulers.Schedulers
import org.learning.reactive.extensions.core._
import org.reactivestreams.{Subscriber, Subscription}

import java.util.concurrent.atomic.AtomicInteger
import scala.concurrent.duration._

object Example1 extends App {

  Observable.range(start = 1, count = 999999999)
    .subscribeOn(Schedulers.computation())
    .map(MyItem(_))
    //.observeOn(Schedulers.io())
    .subscribe { myItem =>
      sleep(500.milliseconds)
      log(msg = s"Received: $myItem")
    }

  sleep(12.seconds)

}

object Example2 extends App {

  Flowable.range(start = 1, count = 999999999)
    .map(MyItem(_))
    .observeOn(Schedulers.io())
    .subscribe { myItem =>
      sleep(50.milliseconds)
      log(msg = s"Received: $myItem")
    }

  sleep(12.seconds)

}

object Example3 extends App {

  Flowable.interval(1.millisecond)
    .observeOn(Schedulers.io())
    .map(intenseCalculation)
    .subscribe(
      n => log(msg = s"$n"),
      _.printStackTrace())

  sleep(12.seconds)

}

object Example4 extends App {

  Flowable.range(start = 1, count = 1000)
    .doOnNext(n => log(msg = s"Source pushed: $n"))
    .observeOn(Schedulers.io())
    .map(mediumIntenseCalculation)
    .subscribe(
      n => log(msg = s"Subscriber received: $n"),
      _.printStackTrace(),
      log(msg = "Done!"))

  sleep(24.seconds)

}

object Example5 extends App {

  Flowable.range(start = 1, count = 1000)
    .doOnNext(n => log(msg = s"Source pushed: $n"))
    .observeOn(Schedulers.io())
    .map(mediumIntenseCalculation)
    .subscribe(new MySubscriber)

  sleep(24.seconds)

  class MySubscriber extends Subscriber[Int] {

    override def onSubscribe(subscription: Subscription): Unit =
      subscription request Long.MaxValue

    override def onNext(n: Int): Unit = {
      sleep(50.milliseconds)
      log(msg = s"Subscriber received: $n")
    }

    override def onError(e: Throwable): Unit =
      e.printStackTrace()

    override def onComplete(): Unit =
      log(msg = "Done!")

  }

}

object Example6 extends App {

  Flowable.range(start = 1, count = 1000)
    .doOnNext(n => log(msg = s"Source pushed: $n"))
    .observeOn(Schedulers.io())
    .map(mediumIntenseCalculation)
    .observeOn(Schedulers.computation())
    .subscribe(new MySubscriber)

  sleep(24.seconds)

  class MySubscriber extends Subscriber[Int] {

    private val count = new AtomicInteger(0)

    private var subscription: Subscription = _

    override def onSubscribe(subscription: Subscription): Unit = {
      this.subscription = subscription

      log(msg = "Requesting 40 items!")
      subscription request 40
    }

    override def onNext(n: Int): Unit = {
      sleep(50.milliseconds)
      log(msg = s"Subscriber received: $n")

      if (count.incrementAndGet() >= 40 && count.get() % 20 == 0) {
        log(msg = s"Requesting more 20 items!")
        subscription request 20
      }
    }

    override def onError(e: Throwable): Unit =
      e.printStackTrace()

    override def onComplete(): Unit =
      log(msg = s"Done!")

  }

}

class MyItem(val id: Int) {

  override def toString: String =
    s"MyItem(id = $id)"

}

object MyItem {

  def apply(id: Int): MyItem = {
    log(msg = s"Constructing MyItem $id")
    new MyItem(id)
  }

}
