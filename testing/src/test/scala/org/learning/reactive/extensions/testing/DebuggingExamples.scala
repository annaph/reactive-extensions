package org.learning.reactive.extensions.testing

import org.junit.Test
import org.learning.reactive.extensions.core.testing.TestObserver
import org.learning.reactive.extensions.core.{Observable, log}

class DebuggingExamples {

  @Test
  def debugWalkThrough(): Unit = {
    val testObserver = TestObserver.create[String]

    val items = "521934/2342/Foxtrot" :: "Bravo/12112/78886/Tango" :: "283242/4542/Whiskey/2348562" :: Nil

    Observable.from(items)
      //.doOnNext(str => log(msg = s"Source pushed: $str"))
      .concatMap(str => Observable from str.split("/").toIndexedSeq)
      //.doOnNext(str => log(msg = s"concatMap() pushed: $str"))
      .filter(_ matches "[A-Za-z]+")
      //.doOnNext(str => log(msg = s"filter() pushed: $str"))
      .subscribe(testObserver)

    log(msg = s"Values: ${testObserver.values mkString ","}")

    testObserver.assertValues(values = "Foxtrot", "Bravo", "Tango", "Whiskey")

  }

}
