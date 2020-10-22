package org.learning.reactive.extensions.multicasting

import org.learning.reactive.extensions.core.{Observable, Subject, log, sleep}

import scala.concurrent.duration._

object Example13 extends App {

  val subject = Subject.publishSubject[String]()

  subject.map(_.length)
    .subscribe(n => log(msg = s"Received: $n"))

  subject onNext "Alpha"
  subject onNext "Beta"
  subject onNext "Gamma"

  subject.onComplete()

}

object Example14 extends App {

  val source1 = Observable.interval(1.seconds)
    .map(n => s"${n + 1} seconds")

  val source2 = Observable.interval(300.milliseconds)
    .map(n => s"${(n + 1) * 300} milliseconds")

  val subject = Subject.publishSubject[String]()

  subject.subscribe(str => log(s"Received: $str"))

  source1 subscribe subject
  source2 subscribe subject

  sleep(3.seconds)

}

object Example15 extends App {

  val subject = Subject.publishSubject[String]()

  subject onNext "Alpha"
  subject onNext "Beta"
  subject onNext "Gamma"

  subject.onComplete()

  subject.map(_.length)
    .subscribe(n => log(msg = s"Received: $n"))

}

object Example16 extends App {

  val subject = Subject.behaviorSubject[String]()

  // Observer 1
  subject.subscribe(str => log(msg = s"Observer 1: $str"))

  subject onNext "Alpha"
  subject onNext "Beta"
  subject onNext "Gamma"

  // Observer 2
  subject.subscribe(str => log(msg = s"Observer 2: $str"))

}

object Example17 extends App {

  val subject = Subject.replaySubject[String]()

  // Observer 1
  subject.subscribe(str => log(msg = s"Observer 1: $str"))

  subject onNext "Alpha"
  subject onNext "Beta"
  subject onNext "Gamma"
  subject.onComplete()

  // Observer 2
  subject.subscribe(str => log(msg = s"Observer 2: $str"))

}

object Example18 extends App {

  val subject = Subject.asyncSubject[String]()

  // Observer 1
  subject.subscribe(
    str => log(msg = s"Observer 1: $str"),
    _.printStackTrace(),
    log(msg = "Observer 1 done!"))

  subject onNext "Alpha"
  subject onNext "Beta"
  subject onNext "Gamma"
  subject.onComplete()

  // Observer 2
  subject.subscribe(
    str => log(msg = s"Observer 2: $str"),
    _.printStackTrace(),
    log(msg = "Observer 2 done!"))

}

object Example19 extends App {

  val source = Observable.interval(250.milliseconds)
    .map(n => s"${(n + 1) * 250} milliseconds")

  val subject = Subject.unicastSubject[String]()
  source subscribe subject

  sleep(2.seconds)

  // Observer 1
  subject.subscribe(str => log(msg = s"Observer 1: $str"))

  sleep(1.seconds)

  // Observer 2 will throw: java.lang.IllegalStateException: Only a single observer allowed
  // subject.subscribe(str => log(msg = s"Observer 2: $str"))

  sleep(1.seconds)

}

object Example20 extends App {

  val source = Observable.interval(350.milliseconds)
    .map(n => s"${(n + 1) * 350} milliseconds")

  val subject = Subject.unicastSubject[String]()
  source subscribe subject

  sleep(2.seconds)

  val multicast = subject.publish().autoConnect()

  // Observer 1
  multicast.subscribe(str => log(msg = s"Observer 1: $str"))

  sleep(2.seconds)

  // Observer 2
  multicast.subscribe(str => log(msg = s"Observer 2: $str"))

  sleep(1.seconds)

}
