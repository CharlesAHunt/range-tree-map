import com.charlesahunt.RangeTreeMap
import org.scalatest.WordSpec

import scala.math.Ordering._

class RangeTreeMapTest extends WordSpec {

  "A RangeTreeMap" when {
    "empty" should {
      "have span None" in {
        assert(RangeTreeMap[Int, Int].span() == None)
      }

      "put an element and retrieve it from the map" in {
        //TODO
      }
    }
  }
}