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

import scala.util.Failure
import scala.util.Try

import com.alejandrohdezma.sbt.github.error.NotFound
import com.alejandrohdezma.sbt.github.github.error.GithubError
import com.alejandrohdezma.sbt.github.syntax.scalatry._
import org.specs2.mutable.Specification

class TrySyntaxSpec extends Specification {

  "collectFail" should {

    "change value if Failure" >> {
      val failure = Failure(NotFound)

      val result = failure.collectFail {
        case _ => GithubError("miau")
      }

      result must beAFailedTry(equalTo(GithubError("miau")))
    }

    "do nothing if Success" >> {
      val success = Try(42)

      val result = success.collectFail {
        case _ => NotFound
      }

      result must beSuccessfulTry(42)
    }

  }

  "mapFail" should {

    "change value if Failure" >> {
      val failure = Failure(NotFound)

      val result = failure.mapFail(_ => GithubError("url"))

      result must beAFailedTry(equalTo(GithubError("url")))
    }

    "do nothing if Success" >> {
      val success = Try(42)

      val result = success.mapFail(_ => NotFound)

      result must beSuccessfulTry(42)
    }

  }

  "failAs" should {

    "change value if Failure" >> {
      val failure = Failure(NotFound)

      val result = failure.failAs(GithubError("miau"))

      result must beAFailedTry(equalTo(GithubError("miau")))
    }

    "do nothing if Success" >> {
      val success = Try(42)

      val result = success.failAs(NotFound)

      result must beSuccessfulTry(42)
    }

  }

}
