package com.charlesahunt

import scala.collection.mutable

/**
  * A data structure backed by a TreeMap where numeric ranges map to values and where the key, K, is the lower bound.
  *
  * @tparam K some number type
  * @tparam V any value
  */
class TreeRangeMap[K, V] {

  val rangeMap = new mutable.TreeMap[K, RangeEntry[K, V]]

  /**
    * Returns a view of this range map as an unmodifiable Map[Range[K], V].
    */
  def asDescendingMapOfRanges(): Map[Range[K], V] = {} //TODO

  /**
    * Returns a view of this range map as an unmodifiable Map[Range[K], V].
    */
  def asMapOfRanges(): Map[Range[K], V] = {} //TODO

  def clear(): Unit = rangeMap.clear

  def get(key: K): Option[V] = rangeMap.get(key).map(_.value)

  def put(range: Range[K], value: V): Unit = rangeMap.put(range.lower, RangeEntry(range, value))

  /**
    * Puts all the associations from rangeMap into this range map.
    */
  def putAll(rangeMap: TreeRangeMap[K, V]): Unit = {} //TODO

  /**
    * Maps a range to a specified value, coalescing this range with any existing ranges with the same value that are connected to this range.
    */
  def putCoalescing(range: Range[K], value: V): Unit = {} //TODO

  def remove(rangeToRemove: Range[K]): Unit = rangeMap.remove(rangeToRemove.lower)

  /**
    * Returns the minimal range enclosing the ranges in this RangeMap.
    */
  def span(): Range[K] = {} //TODO

  /**
    * Returns a view of the part of this range map that intersects with range.
    */
  def subRangeMap(subRange: Range[K]): TreeRangeMap[K,V] = {} //TODO

}

object TreeRangeMap {
  def apply[K, V]() = new TreeRangeMap[K, V]
}

case class Range[K](lower: K, upper: K)

case class RangeEntry[K, V](
  range: Range[K],
  value: V
)