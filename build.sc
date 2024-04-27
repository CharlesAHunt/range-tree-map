import mill._, scalalib._

object rangetreemap extends ScalaModule {

  def scalaVersion = "2.13.13"
  def ivyDeps = Agg()

  object test extends Tests {
    def ivyDeps = Agg(
      ivy"org.scalatest::scalatest:3.2.18")
    def testFrameworks = Seq("org.scalatest.tools.Framework")
  }

}