import org.scalatest.{Matchers, WordSpec}
import RangeTreeMapFixture._

class RangeTreeMapTest extends WordSpec with Matchers  {

  "A RangeTreeMap" when {
    "empty" should {

      "have an empty span" in {
        emptyRangeTreeMap.span() shouldBe empty
      }

      "put an element and retrieve it from the map by lower bound" in {
        val testMap = emptyRangeTreeMap
        testMap.put(testRange0_5, "test")
        testMap.get(testRange0_5.lower) should contain ("test")
      }

      "put an element and retrieve it from the map by RangeKey" in {
        val testMap = emptyRangeTreeMap
        testMap.put(testRange0_5, "test")
        testMap.get(testRange0_5) should contain ("test")
      }

      "put and element in the map, retrieve it, then clear the map" in {
        val testMap = emptyRangeTreeMap
        testMap.put(testRange0_5, "test")
        testMap.get(testRange0_5) should contain ("test")
        testMap.clear()
        testMap.get(testRange0_5) shouldBe empty
      }

      "put and element in the map, retrieve it, then remove it" in {
        val testMap = emptyRangeTreeMap
        testMap.put(testRange0_5, "test")
        testMap.get(testRange0_5) should contain ("test")
        testMap.remove(testRange0_5)
        testMap.get(testRange0_5) shouldBe empty
      }

      "put all of another RangeTreeMap elements in the map, then retrieve it" in {
        val testMap1 = emptyRangeTreeMap
        val testMap2 = emptyRangeTreeMap
        testMap1.put(testRange0_5, "test1")
        testMap2.put(testRange6_10, "test2")
        testMap2.put(testRange11_13, "test3")
        testMap1.putAll(testMap2)
        testMap1.get(testRange0_5) should contain ("test1")
        testMap1.get(testRange6_10) should contain ("test2")
        testMap1.get(testRange11_13) should contain ("test3")
      }
    }

    "nonempty" should {

      //TODO: Nonempty test cases

    }
  }
}