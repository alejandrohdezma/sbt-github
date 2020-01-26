package com.alejandrohdezma.sbt.github.syntax

object list {

  implicit class ListTraverseEither[A](private val list: List[A]) extends AnyVal {

    def traverse[B, C](f: A => Either[B, C]): Either[B, List[C]] =
      list.map(f).foldLeft[Either[B, List[C]]](Right(List())) { (acc, el) =>
        acc.flatMap(list => el.map(list :+ _))
      }

  }

}
