package com.alejandrohdezma.sbt.me.github

import org.specs2.mutable.Specification

class CollaboratorsSpec extends Specification {

  "Collaborators.include" should {

    "return list of collaborators including new ones" >> {
      val collaborators = Collaborators(
        List(
          Collaborator("me", "Me", "example.com/me"),
          Collaborator("you", "You", "example.com/you")
        )
      )

      val extra = List(Collaborator("him", "Him", "example.com/him"))

      val expected = Collaborators(
        List(
          Collaborator("him", "Him", "example.com/him"),
          Collaborator("me", "Me", "example.com/me"),
          Collaborator("you", "You", "example.com/you")
        )
      )

      collaborators.include(extra) must be equalTo expected
    }

    "remove duplicates" >> {
      val collaborators = Collaborators(
        List(
          Collaborator("me", "Me", "example.com/me"),
          Collaborator("you", "You", "example.com/you")
        )
      )

      val extra = List(Collaborator("me", "MeMe", "example.com/meme"))

      val expected = Collaborators(
        List(
          Collaborator("me", "Me", "example.com/me"),
          Collaborator("you", "You", "example.com/you")
        )
      )

      collaborators.include(extra) must be equalTo expected
    }

  }

  "Collaborators.markdown" should {

    "return collaborator list as markdown" >> {
      val collaborators = Collaborators(
        List(
          Collaborator("her", "Her", "example.com/her", avatar = Some("example.com/her.png")),
          Collaborator("him", "Him", "example.com/him"),
          Collaborator(
            "it",
            "It",
            "example.com/it",
            Some("it@example.com"),
            Some("example.com/it.png")
          ),
          Collaborator("me", "Me", "example.com/me", Some("me@example.com")),
          Collaborator("you", "", "example.com/you")
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
