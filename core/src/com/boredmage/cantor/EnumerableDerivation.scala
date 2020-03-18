package com.boredmage.cantor

import magnolia.{CaseClass, Magnolia, SealedTrait}

import scala.language.experimental.macros

trait EnumerableDerivation {
  private def cross(xss: Seq[Seq[Any]]): Seq[Seq[Any]] = {
    if (xss.isEmpty) Nil
    else if (xss.size == 1) xss.head.map(List(_))
    else {
      val xs = xss.head
      val rests = xss.tail
      val restsCrossed = cross(rests)
      for {
        x <- xs
        rest <- restsCrossed
      } yield x +: rest
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
