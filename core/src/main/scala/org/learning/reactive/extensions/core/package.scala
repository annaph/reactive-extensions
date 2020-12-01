package org.learning.reactive.extensions

import java.io.{BufferedWriter, File, FileWriter}
import java.util.concurrent.ThreadLocalRandom

import scala.concurrent.duration.{Duration, _}
import scala.io.Source
import scala.util.{Failure, Try}

package object core {

  def log(msg: String): Unit =
    println(s"${Thread.currentThread.getName}: $msg")

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
