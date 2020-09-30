package org.learning.reactive.extensions.combining.observables

import org.learning.reactive.extensions.core.{GroupedObservable, Observable, Single, log}

object Example21 extends App {

  val byLengths = Observable.from("Alpha", "Beta", "Gamma", "Delta", "Epsilon")
    .groupBy(_.length)
    .flatMapSingle(_.toList())
    .subscribe(l => log(s"${l.mkString("[", ",", "]")}"))

}

object Example22 extends App {

  val byLengths = Observable.from("Alpha", "Beta", "Gamma", "Delta", "Epsilon")
    .groupBy(_.length)
    .flatMapSingle(flatGroup)
    .subscribe(log _)

  def flatGroup(group: GroupedObservable[Int, String]): Single[String] =
    group.toList()
      .map(_ mkString ", ")
      .map(s"${group.getKey}: " + _)

}
