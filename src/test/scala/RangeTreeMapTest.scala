import com.charlesahunt.{RangeEntry, RangeKey, RangeTreeMap}
import org.scalatest.{Matchers, WordSpec}

import scala.math.Ordering._

class RangeTreeMapTest extends WordSpec with Matchers  {

  "A RangeTreeMap" when {
    "empty" should {
      "have an empty span" in {
        RangeTreeMap.apply[Int, String].span() shouldBe empty
      }

      "put an element and retrieve it from the map" in {
        val testRange = RangeKey[Int](0, 5)
        val testMap = RangeTreeMap.apply[Int, String]
        testMap.put(testRange, "test")
        testMap.get(testRange) should contain ("test")
      }
    }
  }
}