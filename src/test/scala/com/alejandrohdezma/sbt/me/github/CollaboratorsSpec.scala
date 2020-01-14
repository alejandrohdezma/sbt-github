package com.alejandrohdezma.sbt.me.github

import org.specs2.mutable.Specification

class CollaboratorsSpec extends Specification {

  "Collaborators.include" should {

    "return list of collaborators including new ones" >> {
      val collaborators = Collaborators(
        List(
          new Collaborator("me", "example.com/me", "", Some("Me"), None, None),
          new Collaborator("you", "example.com/you", "", Some("You"), None, None)
        )
      )

      val extra = List(new Collaborator("him", "example.com/him", "", Some("Him"), None, None))

      val expected = Collaborators(
        List(
          new Collaborator("him", "example.com/him", "", Some("Him"), None, None),
          new Collaborator("me", "example.com/me", "", Some("Me"), None, None),
          new Collaborator("you", "example.com/you", "", Some("You"), None, None)
        )
      )

      collaborators.include(extra) must be equalTo expected
    }

    "remove duplicates" >> {
      val collaborators = Collaborators(
        List(
          new Collaborator("me", "example.com/me", "", Some("Me"), None, None),
          new Collaborator("you", "example.com/you", "", Some("You"), None, None)
        )
      )

      val extra = List(new Collaborator("me", "example.com/meme", "", Some("MeMe"), None, None))

      val expected = Collaborators(
        List(
          new Collaborator("me", "example.com/me", "", Some("Me"), None, None),
          new Collaborator("you", "example.com/you", "", Some("You"), None, None)
        )
      )

      collaborators.include(extra) must be equalTo expected
    }

  }

  "Collaborators.markdown" should {

    "return collaborator list as markdown" >> {
      val collaborators = Collaborators(
        List(
          new Collaborator(
            "her",
            "example.com/her",
            "",
            Some("Her"),
            None,
            Some("example.com/her.png")
          ),
          new Collaborator("him", "example.com/him", "", Some("Him"), None, None),
          new Collaborator(
            "it",
            "example.com/it",
            "",
            Some("It"),
            Some("it@example.com"),
            Some("example.com/it.png")
          ),
          new Collaborator("me", "example.com/me", "", Some("Me"), Some("me@example.com"), None),
          new Collaborator("you", "example.com/you", "", Some(""), None, None)
        )
      )

      val markdown = collaborators.markdown

      val expected =
        """- [![her](example.com/her.png&s=20) **Her (her)**](example.com/her)
          |- [**Him (him)**](example.com/him)
          |- [![it](example.com/it.png&s=20) **It (it)**](example.com/it)
          |- [**Me (me)**](example.com/me)
          |- [**you**](example.com/you)""".stripMargin

      markdown must be equalTo expected
    }

  }

}
