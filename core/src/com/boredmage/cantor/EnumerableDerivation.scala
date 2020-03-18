package com.boredmage.cantor

import magnolia.{CaseClass, Magnolia, SealedTrait}

import scala.language.experimental.macros

trait EnumerableDerivation {
  def cross(xss: Seq[Seq[Any]]): Seq[Seq[Any]] = {
    val startingIndices = xss.scanLeft(0)((i, xs) => i + xs.size)
    val width = xss.size
    val productLength = xss.map(_.size).product
    0.until(productLength).map { n =>
      0.until(width).map { i =>
        val items = xss(i)
        items((n / math.max(1, startingIndices(i))) % items.size)
      }
    }
  }

  type Typeclass[T] = Enumerable[T]
  def combine[T](caseClass: CaseClass[Typeclass, T]): Typeclass[T] = new Typeclass[T] {
    override val values: IndexedSeq[T] =
      if (caseClass.parameters.isEmpty) // Objects and empty case classes are really ()
        IndexedSeq(caseClass.rawConstruct(Nil))
      else
        cross(caseClass.parameters.map(p => p.typeclass.values)).map(caseClass.rawConstruct).toIndexedSeq
  }

  def dispatch[T](sealedTrait: SealedTrait[Typeclass, T]): Typeclass[T] = new Typeclass[T] {
    override val values: IndexedSeq[T] = sealedTrait.subtypes.flatMap(t => t.typeclass.values).toIndexedSeq
  }

  implicit def gen[T]: Typeclass[T] = macro Magnolia.gen[T]
}
