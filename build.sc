import mill._, scalalib._

import $ivy.`com.lihaoyi::mill-contrib-bloop:$MILL_VERSION`

object core extends ScalaModule {
  def scalaVersion = "2.13.1"
  def scalacOptions = Seq(
    "-deprecation",
    "-feature",
    "-language:higherKinds"
  )

  def ivyDeps = Agg(ivy"com.propensive::magnolia::0.12.8")

  object test extends Tests {
    def ivyDeps = Agg(ivy"org.scalatest::scalatest:3.1.1")
    def testFrameworks = Seq("org.scalatest.tools.Framework")
  }
}