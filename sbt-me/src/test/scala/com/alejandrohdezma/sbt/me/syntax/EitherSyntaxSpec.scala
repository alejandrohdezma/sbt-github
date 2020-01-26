package com.alejandrohdezma.sbt.me.syntax

import com.alejandrohdezma.sbt.me.syntax.either._
import org.specs2.mutable.Specification

class EitherSyntaxSpec extends Specification {

  "leftMap" should {

    "change value if Left" >> {
      val either = Left(40)

      either.leftMap(_ + 2) must beLeft(42)
    }

    "do nothing if Right" >> {
      val either = Right[Int, Int](40)

      either.leftMap(_ + 2) must beRight(40)
    }

  }

  "onLeft" should {

    "execute consumer if Left" >> {
      val either = Left(40)

      either.onLeft(_ => sys.error("fail")) must throwA[RuntimeException]("fail")
    }

    "do nothing if Right" >> {
      val either = Right[Int, Int](40)

      either.onLeft(_ => sys.error("fail")) must beRight(40)
    }

  }

}
