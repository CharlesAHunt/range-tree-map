import org.scalatest.WordSpec

class RangeTreeMapTestSpec extends WordSpec {

  "A RangeTreeMap" when {
    "empty" should {
      "have size 0" in {
        assert(RangeTreeMap.empty.size == 0)
      }

      "produce NoSuchElementException when head is invoked" in {
        assertThrows[NoSuchElementException] {
          Set.empty.head
        }
      }
    }
  }
}