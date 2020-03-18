package com.boredmage.cantor

trait Enumerable[A] {
  val values: IndexedSeq[A]
}

object Enumerable extends EnumerableInstances with EnumerableDerivation {
  def apply[A: Enumerable]: Enumerable[A] = implicitly[Enumerable[A]]

  def lift[A](as: IndexedSeq[A]): Enumerable[A] = new Enumerable[A] {
    override val values: IndexedSeq[A] = as
  }

  def lift[A](values: A*): Enumerable[A] = lift(values.toIndexedSeq)
}
