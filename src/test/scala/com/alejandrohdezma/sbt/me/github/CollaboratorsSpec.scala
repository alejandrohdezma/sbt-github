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

}
