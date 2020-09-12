package org.learning.reactive.extensions.basic.operators

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import org.learning.reactive.extensions.core.{Observable, log}

object Example14 extends App {

  val formatter = DateTimeFormatter ofPattern "M/d/yyyy"

  Observable.just("1/3/2016", "5/9/2016", "10/12/2016")
    .map(s => LocalDate.parse(s, formatter))
    .subscribe(date => log(s"RECEIVED: $date"))

}

object Example15 extends App {

  Observable.from(List("Coffee", "Tea", "Espresso", "Latte"))
    .startWithItem("COFFEE SHOP MENU")
    .subscribe(log _)

}

object Example16 extends App {

  Observable.from(List("Coffee", "Tea", "Espresso", "Latte"))
    .startWith("COFFEE SHOP MENU", "----------------")
    .subscribe(log _)

  Observable.from(List("Coffee", "Tea", "Espresso", "Latte"))
    .startWith(List("COFFEE SHOP MENU", "----------------"))
    .subscribe(log _)

}

object Example17 extends App {

  Observable.from(List(6, 2, 5, 7, 1, 4, 9, 8, 3))
    .sorted
    .subscribe(n => log(s"$n"))

}

object Example18 extends App {

  Observable.from(List(6, 2, 5, 7, 1, 4, 9, 8, 3))
    .sorted(Ordering.Int.reverse)
    .subscribe(n => log(s"$n"))

}

object Example19 extends App {

  Observable.just("Alpha", "Beta", "Gamma")
    .sorted(_.length)
    .subscribe(n => log(s"$n"))

}

object Example20 extends App {

  Observable.just(5, 3, 7)
    .scan(_ + _)
    .subscribe(n => log(s"RECEIVED: $n"))

}

object Example21 extends App {

  Observable.just("Alpha", "Beta", "Gamma")
    .scanLeft(0)((acc, _) => acc + 1)
    .subscribe(n => log(s"RECEIVED: $n"))

}
