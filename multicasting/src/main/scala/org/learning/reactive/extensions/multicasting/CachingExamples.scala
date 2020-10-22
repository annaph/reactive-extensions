package org.learning.reactive.extensions.multicasting

import org.learning.reactive.extensions.core.{Observable, log}

object Example12 extends App {

  val cachedRollingTotals = Observable.from(items = 6, 2, 5, 7, 1, 4, 9, 8, 3)
    .scanLeft(0)(_ + _)
    //.cache()
    .cacheWithInitialCapacity(initialCapacity = 10)

  cachedRollingTotals.subscribe(n => log(s"Received: $n"))

}
