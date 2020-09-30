package org.learning.reactive.extensions.combining.observables

import org.learning.reactive.extensions.core.{Observable, log, sleep}

import scala.concurrent.duration._

object Example15 extends App {

  Observable.amb(Example13.src1, Example13.src2)
    .subscribe(str => log(s"Received: $str"))

  sleep(7.seconds)

}

object Example16 extends App {

  Example13.src1
    .ambWith(Example13.src2)
    .subscribe(str => log(s"Received: $str"))

  sleep(7.seconds)

}
