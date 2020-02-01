/*
 * Copyright 2019-2020 Alejandro Hernández <https://github.com/alejandrohdezma>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alejandrohdezma.sbt.github.syntax

import com.alejandrohdezma.sbt.github.syntax.either._
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
