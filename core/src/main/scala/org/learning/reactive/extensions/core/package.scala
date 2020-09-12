package org.learning.reactive.extensions

import scala.concurrent.duration.Duration
import scala.util.{Failure, Try}

package object core {

  def log(msg: String): Unit =
    println(s"${Thread.currentThread.getName}: $msg")

  def sleep(period: Duration): Unit = Try(Thread sleep period.toMillis) match {
    case Failure(e) =>
      e.printStackTrace()
    case _ =>
  }

}
