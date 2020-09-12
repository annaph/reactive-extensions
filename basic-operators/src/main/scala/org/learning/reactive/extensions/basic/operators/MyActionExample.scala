package org.learning.reactive.extensions.basic.operators

import java.util.concurrent.Executors

import io.reactivex.rxjava3.schedulers.Schedulers
import org.learning.reactive.extensions.core.{Observable, log}

import scala.concurrent.{ExecutionContext, Future}

object MyActionExample extends App {

  var i = 0

  def myAction(): Unit = {
    //Thread sleep 1000
    if (i == 7) throw new Exception

    i = i + 1
    log(s"My action '$i'...")

    Observable.just(1, 2, 3)
      .observeOn(Schedulers.computation())
      .map(n => {
        log(s"[Action-$i] Processing '$n'...")
        n.toString
      })
      .observeOn(Schedulers.io())
      .subscribe(s => log(s"[Action-$i] Received: $s"))

    log(s"My action '$i' done.")
  }

  implicit val _exeCtx: ExecutionContext = ExecutionContext fromExecutor Executors.newFixedThreadPool(1)

  Future {
    Observable.fromAction[Unit](myAction())
      .repeat()
      .subscribe(
        _ => (),
        e => log(s"Error occurred: '$e'"),
        log("Done!"))
  }

  Thread sleep 12000

}
