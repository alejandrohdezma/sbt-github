package com.alejandrohdezma.sbt.me.github

import org.specs2.mutable.Specification

class CollaboratorsSpec extends Specification {

  "Collaborators.include" should {

    "return list of collaborators including new ones" >> {
      val collaborators = Collaborators(
        List(
          Collaborator("me", "Me", "me@example.com"),
          Collaborator("you", "You", "you@example.com")
        )
      )

      val extra = List(Collaborator("him", "Him", "him@example.com"))

      val expected = Collaborators(
        List(
          Collaborator("him", "Him", "him@example.com"),
          Collaborator("me", "Me", "me@example.com"),
          Collaborator("you", "You", "you@example.com")
        )
      )

      collaborators.include(extra) must be equalTo expected
    }

    "remove duplicates" >> {
      val collaborators = Collaborators(
        List(
          Collaborator("me", "Me", "me@example.com"),
          Collaborator("you", "You", "you@example.com")
        )
      )

      val extra = List(Collaborator("me", "MeMe", "meme@example.com"))

      val expected = Collaborators(
        List(
          Collaborator("me", "Me", "me@example.com"),
          Collaborator("you", "You", "you@example.com")
        )
      )

      collaborators.include(extra) must be equalTo expected
    }

  }

  "Collaborators.markdown" should {

    "return contributor list as markdown" >> {
      val collaborators = Collaborators(
        List(
          Collaborator("her", "Her", "her@example.com", avatar = Some("example.com/her.png")),
          Collaborator("him", "Him", "him@example.com"),
          Collaborator(
            "it",
            "It",
            "it@example.com",
            Some("example.com/it"),
            Some("example.com/it.png")
          ),
          Collaborator("me", "Me", "me@example.com", Some("example.com/me")),
          Collaborator("you", "", "you@example.com")
        )
      )

      val markdown = collaborators.markdown

      val expected =
        """- ![her](example.com/her.png&s=20) **Her (her)**
          |- **Him (him)**
          |- [![it](example.com/it.png&s=20) **It (it)**](example.com/it)
          |- [**Me (me)**](example.com/me)
          |- **you**""".stripMargin

      markdown must be equalTo expected
    }

  }

}
