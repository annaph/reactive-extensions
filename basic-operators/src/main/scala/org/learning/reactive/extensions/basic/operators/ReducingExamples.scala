package org.learning.reactive.extensions.basic.operators

import java.time.LocalDate
import java.util.concurrent.ConcurrentHashMap

import org.learning.reactive.extensions.core.{Observable, log}

import scala.collection.mutable

object Example22 extends App {

  Observable.just("Alpha", "Beta", "Gamma")
    .count()
    .subscribe(n => log(s"RECEIVED: $n"))

}

object Example23 extends App {

  Observable.just(5, 3, 7)
    .fold(_ + _)
    .subscribe(n => log(s"RECEIVED: $n"))

}

object Example24 extends App {

  val source = Observable.just(5, 3, 7).foldLeft("") {
    case ("", n) =>
      s"$n"
    case (acc, n) =>
      acc + s",$n"
  }

  source.subscribe(s => log(s"RECEIVED: $s"))

}

object Example25 extends App {

  Observable.from(List(5, 3, 7, 11, 2, 14))
    .forall(_ < 10)
    .subscribe(b => log(s"Received: $b"))

}

object Example26 extends App {

  Observable.from(List("2020-01-01", "2020-05-02", "2020-09-12", "2020-04-03"))
    .map(LocalDate.parse)
    .exists(_.getMonthValue >= 6)
    .subscribe(d => log(s"Received: $d"))

}

object Example27 extends App {

  Observable.just("One", "Two", "Three")
    .filter(_ contains 'z')
    .isEmpty()
    .subscribe(b => log(s"Received1: $b"))

  Observable.just("One", "Twoz", "Three")
    .filter(_ contains 'z')
    .isEmpty()
    .subscribe(b => log(s"Received2: $b"))

}

object Example28 extends App {

  Observable.range(1, 10000)
    .contains(9563)
    .subscribe(b => log(s"Received: $b"))

}

object Example29 extends App {

  val source1 = Observable.just("One", "Two", "Three")
  val source2 = Observable.just("One", "Two", "Three")
  val source3 = Observable.just("Two", "One", "Three")
  val source4 = Observable.just("One", "Two")

  Observable.sequenceEqual(source1, source2).subscribe(b => log(s"Received: $b"))
  Observable.sequenceEqual(source1, source3).subscribe(b => log(s"Received: $b"))
  Observable.sequenceEqual(source1, source4).subscribe(b => log(s"Received: $b"))

}

object Example30 extends App {

  Observable.just("Alpha", "Beta", "Gamma")
    .toList()
    .subscribe(l => log(s"Received: ${l.mkString("[", ", ", "]")}"))

}

object Example31 extends App {

  Observable.range(1, 12)
    .toList(12)
    .subscribe(l => log(s"Received: $l"))

}

object Example32 extends App {

  import java.util.concurrent.CopyOnWriteArrayList

  Observable.just("Beta", "Gamma", "Alpha")
    .toListJava(new CopyOnWriteArrayList[String])
    .subscribe(l => log(s"Received: $l"))

}

object Example33 extends App {

  Observable.just("Beta", "Gamma", "Alpha")
    .toSortedList()
    .subscribe(l => log(s"Received: $l"))

}

object Example34 extends App {

  Observable.just("Alpha", "Beta", "Gamma")
    .toMap[Char](_.charAt(0))
    .subscribe(m => log(s"Received: $m"))

}

object Example35 extends App {

  Observable.just("Alpha", "Beta", "Gamma")
    .toMap[Char, Int](_.charAt(0))(_.length)
    .subscribe(m => log(s"Received: $m"))

}

object Example36 extends App {

  Observable.just("Alpha", "Beta", "Gamma")
    .toMapJava[Char, Int](new ConcurrentHashMap)(_.charAt(0))(_.length)
    .subscribe(m => log(s"Received: $m"))

}

object Example37 extends App {

  Observable.just("Alpha", "Beta", "Gamma")
    .toMap[Int](_.length)
    .subscribe(m => log(s"Received: $m"))

}

object Example38 extends App {

  Observable.just("Alpha", "Beta", "Gamma")
    .toMultimap(_.length)
    .subscribe(m => log(s"Received: $m"))

}

object Example39 extends App {

  Observable.from(List("Alpha", "Beta", "Gamma", "Beta"))
    .collect(mutable.HashSet.empty[String])(_ += _)
    .subscribe(s => log(s"Received: $s"))

}

object Example40 extends App {

  Observable.just("Alpha", "Beta", "Gamma")
    .collect(new ImmutableListBuilder)(_ += _)
    .map(_.result())
    .subscribe(l => log(s"Received: $l"))

  class ImmutableListBuilder(private var _list: List[String] = List.empty)
    extends mutable.ImmutableBuilder[String, List[String]](_list) {

    override def addOne(elem: String): this.type = {
      _list = elem :: _list
      this
    }

    override def result(): List[String] =
      _list.reverse

  }

}
