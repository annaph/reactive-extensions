package org.learning.reactive.extensions

package object core {

  def log(msg: String): Unit =
    println(s"${Thread.currentThread.getName}: $msg")

}
