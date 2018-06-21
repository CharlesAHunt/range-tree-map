import mill._, scalalib._

object rangetreemap extends ScalaModule {
  def scalaVersion = "2.12.5"
  def ivyDeps = Agg(
    ivy"com.lihaoyi::upickle:0.5.1",
    ivy"org.scalatest::scalatest:3.0.5"
  )
}