import org.scalatest.{Matchers, WordSpec}
import RangeTreeMapFixture._
import com.charlesahunt.RangeKey

class RangeTreeMapTest extends WordSpec with Matchers  {

  "A RangeTreeMap" when {
    "empty" should {

      "have an empty span" in {
        emptyRangeTreeMap.span() shouldBe empty
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

      "return false if the range is not enclosed" in {
        emptyRangeTreeMap.encloses(RangeKey[Int](2, 77)) should be (false)
      }
    }

    "nonempty" should {

      "return true if the range is enclosed" in {
        nonEmptyRangeTreeMap.encloses(testRange0_5) should be (true)
        nonEmptyRangeTreeMap.encloses(RangeKey[Int](2, 7)) should be (true)
      }

      "return false if the range is not enclosed" in {
        nonEmptyRangeTreeMap.encloses(RangeKey[Int](12, 27)) should be (false)
      }

    }
  }
}