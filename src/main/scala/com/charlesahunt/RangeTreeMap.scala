package com.charlesahunt

import scala.collection.mutable

/**
  * A data structure backed by a mutable TreeMap where numeric ranges map to values and where the key, K, is the lower bound.
  *
  * @param initialMap Optional TreeMap structure to initialize RangeTreeMap
  * @param ordering   A strategy for sorting instances of a type
  * @tparam K some type with an Ordering defined for it
  * @tparam V any value
  */
class RangeTreeMap[K, V](initialMap: Option[mutable.TreeMap[K, RangeEntry[K, V]]] = None)(implicit ordering : scala.Ordering[K]) {

  val rangeTreeMap: mutable.TreeMap[K, RangeEntry[K, V]] = initialMap.getOrElse(new mutable.TreeMap[K, RangeEntry[K, V]])

  /**
    * Returns a view of this range map as an unmodifiable Map[Range[K], V].
    */
  def asMapOfRanges(): Map[RangeKey[K], V] =
    rangeTreeMap.values.map(entry => entry.range -> entry.value).toMap

  def clear(): Unit = rangeTreeMap.clear

  def get(range: RangeKey[K]): Option[V] = rangeTreeMap.get(range.lower).map(_.value)

  /**
    * Puts given value in map. Does not coalesce ranges.
    */
  def put(range: RangeKey[K], value: V): Option[RangeEntry[K, V]] =
    rangeTreeMap.put(range.lower, RangeEntry(range, value))

  /**
    * Puts all the associations from rangeMap into this range map.
    */
  def putAll(rangeMap: RangeTreeMap[K, V]): RangeTreeMap[K, V] = {
    rangeTreeMap.++=(rangeMap.rangeTreeMap)
    this
  }

  def remove(rangeToRemove: RangeKey[K]): Option[RangeEntry[K, V]] =
    rangeTreeMap.remove(rangeToRemove.lower)

  /**
    * Returns the minimal range enclosing the ranges in this RangeMap.
    */
  def span(): Option[RangeKey[K]] =
    rangeTreeMap.headOption.map(head => RangeKey(head._1, rangeTreeMap.last._1))

  /**
    * Returns a view of the part of this range map that intersects with range.
    */
  def subRangeMap(subRange: RangeKey[K]): RangeTreeMap[K, V] =
    RangeTreeMap.apply[K, V](Some(intersection(subRange)))

  /**
    * Maps a range to a specified value, coalescing this range with any existing ranges with the same value that are connected to this range.
    */
  def putCoalescing(range: RangeKey[K], value: V): Option[RangeEntry[K, V]] = {
    if (rangeTreeMap.nonEmpty && encloses(range)) {
      intersection(range).foreach { intersection =>
        //TODO: https://github.com/CharlesAHunt/RangeTreeMap/issues/1
        //TODO Create new subranges from the existing ranges that do not overlap with the new range
        throw new Exception("Coalescing of intersecting ranges not yet implemented.")
      }
    }
    put(range, value)
  }

  /**
    * Checks if the given range is completely, inclusively within the lowest lower bound and the greatest upper bound of the entire RangeTreeMap
    *q
    * 
    * @param range the range to check for enclosure
    * @return True if given range is equal to or within the bounds of the entire RangeTreeMap
    */
  def encloses(range: RangeKey[K]): Boolean = (
    for {
      headLowerBound <- rangeTreeMap.headOption.map(_._2.range.lower)
      lastUpperBound <- rangeTreeMap.lastOption.map(_._2.range.upper)
    } yield {
      (ordering.gteq(range.lower, headLowerBound) && ordering.lteq(range.lower, lastUpperBound)) &&
        (ordering.lteq(range.upper, lastUpperBound) && ordering.gteq(range.upper, headLowerBound))
    })
    .getOrElse(false)

  /**
    * Finds all inclusively intersecting ranges with `subRange` in the map
    *
    * @param subRange
    * @return a TreeMap of all intersecting ranges of `subRange` within the RangeTreeMap
    */
  def intersection(subRange: RangeKey[K]): mutable.TreeMap[K, RangeEntry[K, V]] =
    rangeTreeMap.filter { entry =>
      ordering.lteq(entry._2.range.lower, subRange.upper) && ordering.gteq(entry._2.range.upper, subRange.lower)
    }

}

object RangeTreeMap {

  def apply[K, V](implicit ordering : scala.Ordering[K]): RangeTreeMap[K, V] = new RangeTreeMap[K, V]

  def apply[K, V](initialMap: Option[mutable.TreeMap[K, RangeEntry[K, V]]])(implicit ordering : scala.Ordering[K]): RangeTreeMap[K, V] =
    new RangeTreeMap[K, V](initialMap)

}

final case class RangeKey[K](lower: K, upper: K)

final case class RangeEntry[K, V](
  range: RangeKey[K],
  value: V
)