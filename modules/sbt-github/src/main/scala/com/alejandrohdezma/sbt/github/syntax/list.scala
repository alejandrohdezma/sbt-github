/*
 * Copyright 2019-2022 Alejandro Hernández <https://github.com/alejandrohdezma>
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

import scala.util.Success
import scala.util.Try

object list {

  implicit class ListOps[A](private val list: List[A]) extends AnyVal {

    def traverse[B, C](f: A => Try[B]): Try[List[B]] =
      list.map(f).foldLeft[Try[List[B]]](Success(List())) { (acc, el) =>
        acc.flatMap(list => el.map(list :+ _))
      }

  }

}
