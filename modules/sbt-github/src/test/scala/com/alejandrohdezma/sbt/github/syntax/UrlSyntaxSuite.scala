/*
 * Copyright 2019-2026 Alejandro Hernández <https://github.com/alejandrohdezma>
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

import com.alejandrohdezma.sbt.github.syntax.url._

class UrlSyntaxSuite extends munit.FunSuite {

  test("uri.withQueryParam should add query param to Uri without query") {
    val uri = sbt.url("https://example.com")

    val result = uri.withQueryParam("miau", "42")

    val expected = sbt.url("https://example.com?miau=42")

    assertEquals(result, expected)
  }

  test("uri.withQueryParam should add query param to URL with query") {
    val uri = sbt.url("https://example.com?page=2")

    val result = uri.withQueryParam("miau", "42")

    val expected = sbt.url("https://example.com?page=2&miau=42")

    assertEquals(result, expected)
  }

}
