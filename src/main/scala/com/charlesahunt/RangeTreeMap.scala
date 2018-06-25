package com.charlesahunt

import scala.collection.mutable

/**
  * A data structure backed by a TreeMap where numeric ranges map to values and where the key, K, is the lower bound.
  *
  * @tparam K some number type
  * @tparam V any value
  */
class RangeTreeMap[K, V](implicit ordering : scala.Ordering[K]) {

  val rangeTreeMap: mutable.TreeMap[K, RangeEntry[K, V]] = new mutable.TreeMap[K, RangeEntry[K, V]]

  /**
    * Returns a view of this range map as an unmodifiable Map[Range[K], V].
    */
  def asMapOfRanges(): Map[RangeKey[K], V] =
    rangeTreeMap.values.map(entry => entry.range -> entry.value).toMap

  def clear(): Unit = rangeTreeMap.clear

  def get(key: K): Option[V] = rangeTreeMap.get(key).map(_.value)

  def get(range: RangeKey[K]): Option[V] = get(range.lower)

  def put(range: RangeKey[K], value: V): Option[RangeEntry[K, V]] =
    rangeTreeMap.put(range.lower, RangeEntry(range, value))

  /**
    * Puts all the associations from rangeMap into this range map.
    */
  def putAll(rangeMap: RangeTreeMap[K, V]):  RangeTreeMap[K, V] = {
    rangeTreeMap.++(rangeMap.rangeTreeMap)
    this
  }

  /**
    * Maps a range to a specified value, coalescing this range with any existing ranges with the same value that are connected to this range.
    */
  def putCoalescing(range: RangeKey[K], value: V): Unit = {
    () //TODO
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
  def subRangeMap(subRange: RangeKey[K]): RangeTreeMap[K, V] = {
    rangeTreeMap.keysIteratorFrom(subRange.lower)
    null  //TODO
  }

}

object RangeTreeMap {
  def apply[K, V](implicit ordering : scala.Ordering[K]): RangeTreeMap[K, V] = new RangeTreeMap[K, V]
}

final case class RangeKey[K](lower: K, upper: K)

final case class RangeEntry[K, V](
  range: RangeKey[K],
  value: V
)