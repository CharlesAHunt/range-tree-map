import com.charlesahunt.{RangeKey, RangeTreeMap}

object RangeTreeMapFixture {

  val testRange0_5 = RangeKey[Int](0, 5)
  val testRange2_7 = RangeKey[Int](2, 7)
  val testRange6_10 = RangeKey[Int](6, 10)
  val testRange11_13 = RangeKey[Int](11, 13)


  def emptyRangeTreeMap: RangeTreeMap[Int, String] = RangeTreeMap.apply[Int, String]

  def nonEmptyRangeTreeMap: RangeTreeMap[Int, String] ={
    val r = RangeTreeMap.apply[Int, String]
    r.put(testRange0_5, "test1")
    r.put(testRange6_10, "test2")
    r
  }

}