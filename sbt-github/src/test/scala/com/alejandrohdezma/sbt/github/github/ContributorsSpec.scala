package com.alejandrohdezma.sbt.github.github

import org.specs2.mutable.Specification

class ContributorsSpec extends Specification {

  "Contributors.markdown" should {

    "return contributor list as markdown" >> {
      val contributors = Contributors(
        List(
          Contributor("me", 42, "http://example.com/me", Some("http://example.com/me.png")),
          Contributor("him", 6, "http://example.com/him", None),
          Contributor("you", 2, "http://example.com/you", None)
        )
      )

      val markdown = contributors.markdown

      val expected =
        """- [![me](http://example.com/me.png&s=20) **me**](http://example.com/me)
          |- [**him**](http://example.com/him)
          |- [**you**](http://example.com/you)""".stripMargin

      markdown must be equalTo expected
    }

  }

}
