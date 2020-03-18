package com.boredmage.cantor

import com.boredmage.cantor.Enumerable.lift

trait EnumerableInstances {
  implicit val enumerateNothing: Enumerable[Nothing] = lift()
  implicit val enumerateUnit: Enumerable[Unit] = lift(())
  implicit val enumerateBoolean: Enumerable[Boolean] = lift(true, false)

  implicit def enumerableOption[A: Enumerable]: Enumerable[Option[A]] = lift {
    None +: Enumerable[A].values.map(Some.apply)
  }

  implicit def enumerableEither[L: Enumerable, R: Enumerable]: Enumerable[Either[L, R]] = lift {
    Enumerable[L].values.map(Left(_)) ++ Enumerable[R].values.map(Right(_))
  }
}
