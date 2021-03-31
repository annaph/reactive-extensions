package org.learning.reactive.extensions.custom.operators

import org.learning.reactive.extensions.core.Single.SingleTransformer
import org.learning.reactive.extensions.core.{Observable, log}

object Example13 extends App {

  Observable.from(items = "Alpha", "Beta", "Gamma", "Delta", "Epsilon")
    .toList
    .compose(composer = toVectorTransformer)
    .subscribe(v => log(msg = s"${v.mkString("[", ", ", "]")}"))

  def toVectorTransformer[T]: SingleTransformer[List[T], Vector[T]] = _.map(_.toVector)

}

object Example14 extends App {

  Observable.from(items = "Alpha", "Beta", "Gamma", "Delta", "Epsilon")
    .toList
    .compose(composer = zipWithIndexTransformer)
    .subscribe(l => log(msg = s"${l.mkString("[", ", ", "]")}"))

  def zipWithIndexTransformer[T]: SingleTransformer[List[T], List[(T, Int)]] = _.map(_.zipWithIndex)

}
