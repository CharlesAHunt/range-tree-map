package com.charlesahunt

import scala.collection.mutable

/**
  * A data structure backed by a mutable TreeMap where numeric ranges map to values and where the key, K, is the lower bound.
  *
  * @param initialMap Optional TreeMap structure to initialize RangeTreeMap
  * @param ordering
  * @tparam K some number type
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

  /**
    * @param key - The lower bound of the range to retrieve
    */
  def get(key: K): Option[V] = rangeTreeMap.get(key).map(_.value)

  def get(range: RangeKey[K]): Option[V] = get(range.lower)

  /**
    * Puts given value in map. Does not coalesce ranges.
    */
  def put(range: RangeKey[K], value: V): Option[RangeEntry[K, V]] =
    rangeTreeMap.put(range.lower, RangeEntry(range, value))

  /**
    * Puts all the associations from rangeMap into this range map.
    */
  def putAll(rangeMap: RangeTreeMap[K, V]):  RangeTreeMap[K, V] = {
    rangeTreeMap.++(rangeMap.rangeTreeMap)
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
  def putCoalescing(range: RangeKey[K], value: V): Option[RangeEntry[K, V]] =
    if(rangeTreeMap.isEmpty || !encloses(range))
      put(range, value)
    else {
      val intersections = intersection(range)
      val lowerMatch = intersections.get(range.lower)
      if(intersections.isEmpty && lowerMatch.isEmpty) put(range, value)
      else {
        //TODO: https://github.com/CharlesAHunt/RangeTreeMap/issues/1
        throw new Exception("Coalescing of intersecting ranges not yet implemented.")
      }
    }

  /**
    * Checks if the given range is within the lowest lower bound and the greatest upper bound of the entire RangeTreeMap
    *
    * @param range the range to check for enclosure
    * @return
    */
  def encloses(range: RangeKey[K]): Boolean =
    if( (ordering.gt(range.lower, rangeTreeMap.head._2.range.lower) && ordering.lt(range.lower, rangeTreeMap.last._2.range.upper)) ||
      (ordering.lt(range.upper, rangeTreeMap.last._2.range.upper) && ordering.gt(range.upper, rangeTreeMap.head._2.range.lower))) true
    else false


  def intersection(subRange: RangeKey[K]) =
    rangeTreeMap.filter(entry =>ordering.lt(entry._2.range.lower, subRange.upper) && ordering.gt(entry._2.range.upper, subRange.lower))

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