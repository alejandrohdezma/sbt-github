addSbtPlugin("de.heikoseeberger" % "sbt-header" % "5.5.0")
sys.props.get("plugin.version") match {
  case Some(x) => addSbtPlugin("com.alejandrohdezma" % "sbt-github-header" % x)
  case _       => sys.error("https://www.scala-sbt.org/1.x/docs/Testing-sbt-plugins.html")
}
