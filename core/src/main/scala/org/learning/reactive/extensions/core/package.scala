package org.learning.reactive.extensions

import java.io.{BufferedWriter, File, FileWriter}
import java.util.concurrent.ThreadLocalRandom
import scala.concurrent.duration._
import scala.io.Source
import scala.util.{Failure, Try}

package object core {

  def log(msg: String): Unit =
    println(s"${Thread.currentThread.getName}: $msg")

  def tryToExecute[T](action: => T)(onError: Throwable => T): T =
    try {
      action
    } catch {
      case e: Exception =>
        onError(e)
    }

  def intenseCalculation[T](value: T): T = {
    val time = ThreadLocalRandom.current().nextInt(0, 3)
    sleep(time.seconds)

    value
  }

  def sleep(period: Duration): Unit = Try(Thread sleep period.toMillis) match {
    case Failure(e) =>
      e.printStackTrace()
    case _ =>
  }

  def mediumIntenseCalculation[T](value: T): T = {
    val time = ThreadLocalRandom.current().nextInt(0, 200)
    sleep(time.milliseconds)

    value
  }

  def randomDuration(): Duration = {
    val random = ThreadLocalRandom.current().nextInt(2000)
    Duration(random, MILLISECONDS)
  }

  def hrefResponse(path: String): String = {
    val source = Source.fromURL(path, "UTF-8")
    val result = source.getLines mkString "\\A"

    source.close()
    result
  }

  def writeToDisk(text: String, path: String): Try[Unit] = Try {
    val writer = new BufferedWriter(new FileWriter(new File(path)))

    writer append text
    writer.close()
  }

}
