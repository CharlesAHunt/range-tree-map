package com.charlesahunt

import scala.collection.immutable.TreeMap
import scala.collection.mutable

/**
  * A data structure backed by a mutable TreeMap where numeric ranges map to values and where the key, K, is the lower bound.
  *
  * @param initialMap Optional TreeMap structure to initialize RangeTreeMap
  * @param ordering   A strategy for sorting instances of a type
  * @tparam K some type with an Ordering defined for it
  * @tparam V any value
  */
class RangeTreeMap[K, V](initialMap: Option[TreeMap[K, RangeEntry[K, V]]] = None)(implicit ordering : scala.Ordering[K]) {

  import RangeTreeMap._

  val rangeTreeMap: mutable.TreeMap[K, RangeEntry[K, V]] =
    new mutable.TreeMap[K, RangeEntry[K, V]].++(initialMap.getOrElse(Map.empty))

  /**
    * Returns a view of this range map as an unmodifiable Map[Range[K], V].
    */
  def asMapOfRanges(): Map[RangeKey[K], V] =
    rangeTreeMap.values.map(entry => entry.range -> entry.value).toMap

  def clear(): Unit = rangeTreeMap.clear

  def get(range: RangeKey[K]): Option[V] = rangeTreeMap.get(range.lower).map(_.value)

  /**
    * Puts given value in map. Does not coalesce ranges.
    *
    * * How does this work?
    * *   1 - Find all intersecting ranges
    * *   2 - Remove all intersecting ranges
    * *   3 - Put all disjoint ranges of intersecting ranges with new range into map
    * *   4 - Put the whole new range
    *
    */
  def put(range: RangeKey[K], value: V): RangeTreeMap[K, V] = {
    if (rangeTreeMap.nonEmpty) {
      intersections(range).foreach { intersecting =>
        rangeTreeMap.remove(intersecting._1)
        disjoint(range, intersecting._2.range).foreach { disjointRange =>
          if(intersection(disjointRange, range).isEmpty)
            rangeTreeMap.put(disjointRange.lower, RangeEntry(disjointRange, intersecting._2.value))
        }
      }
    }
    rangeTreeMap.put(range.lower, RangeEntry(range, value))
    this
  }

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
    * Returns a view of the part of this range map that intersects/overlaps with range.
    */
  def subRangeMap(subRange: RangeKey[K]): RangeTreeMap[K, V] = {
    apply[K, V](intersections(subRange)
      .flatMap(intersect => intersection(intersect._2.range, subRange).map(_ -> intersect._2.value))
      .toMap.map(kv => kv._1.lower -> RangeEntry(kv._1, kv._2))
    )
  }

  /**
    * Maps a range to a specified value, coalescing this range with any existing ranges with the same value that
    *   are connected to this range.
    */
  def putCoalescing(range: RangeKey[K], value: V): RangeTreeMap[K, V] = {
    intersections(range).map { intersection =>
      remove(intersection._2.range)
      //TODO: Minor: This can still produce multiple, new, overlapping ranges with the same value created if there are > 1 intersections with the same value
      if(intersection._2.value == value)
          put(RangeKey(ordering.min(range.lower, intersection._2.range.lower), ordering.max(range.upper, intersection._2.range.upper)), value)
      else {
        if(lt(intersection._2.range.lower, range.lower))
          put(RangeKey(intersection._2.range.lower, range.lower), intersection._2.value)
        if(gt(intersection._2.range.upper, range.upper))
          put(RangeKey(range.upper, intersection._2.range.upper), intersection._2.value)
        put(range, value)
      }
    }
    this
  }

  /**
    * Checks if the given range is completely, inclusively within the lowest lower bound and the greatest upper bound of the entire RangeTreeMap
    *
    * @param range the range to check for enclosure within the entire RangeTreeMap
    * @return True if given range is equal to or within the bounds of the entire RangeTreeMap
    */
  def encloses(range: RangeKey[K]): Boolean = (
    for {
      headLowerBound <- rangeTreeMap.headOption.map(_._2.range.lower)
      lastUpperBound <- rangeTreeMap.lastOption.map(_._2.range.upper)
    } yield {
      (gteq(range.lower, headLowerBound) && lteq(range.lower, lastUpperBound)) &&
        (lteq(range.upper, lastUpperBound) && gteq(range.upper, headLowerBound))
    }).getOrElse(false)

  /**
    * Finds all inclusively intersecting ranges with `subRange` in the map
    *
    * @param subRage the range to check for intersection with any range in RangeTreeMap
    * @return a TreeMap of all intersecting ranges of `subRange` within the RangeTreeMap
    */
  def intersections(subRange: RangeKey[K]): mutable.TreeMap[K, RangeEntry[K, V]] =
    rangeTreeMap.filter { entry =>
      lteq(entry._2.range.lower, subRange.upper) && gteq(entry._2.range.upper, subRange.lower)
    }

  /**
    * Yields the intersection range of rangeKey1 and rangeKey2
    */
  def intersection(rangeKey1: RangeKey[K], rangeKey2: RangeKey[K]): Option[RangeKey[K]] = {
    for {
      upperIntersectionBound <- {
        if (lteq(rangeKey2.upper, rangeKey1.upper) && gteq(rangeKey2.upper, rangeKey1.lower)) Some(rangeKey2.upper)
        else if (gteq(rangeKey2.upper, rangeKey1.upper) && lteq(rangeKey2.lower, rangeKey1.upper)) Some(rangeKey1.upper)
        else None
      }
      lowerIntersectionBound <- {
        if (gteq(rangeKey1.lower, rangeKey2.lower) && lteq(rangeKey1.lower, rangeKey2.upper)) Some(rangeKey1.lower)
        else if (lteq(rangeKey1.lower, rangeKey2.lower) && gteq(rangeKey1.upper, rangeKey2.lower)) Some(rangeKey2.lower)
        else None
      }
    } yield RangeKey(lowerIntersectionBound, upperIntersectionBound)
  }

  /**
    * Yields the disjoint(non-overlapping) ranges of rangeKey1 and rangeKey2
    *
    * There can exist 0, 1, or 2 possible disjoint Ranges from any two given rangeKeys
    *
    */
  def disjoint(rangeKey1: RangeKey[K], rangeKey2: RangeKey[K]): Set[RangeKey[K]] = {
    val sortedRangeKeys = List(rangeKey1.lower, rangeKey1.upper, rangeKey2.lower, rangeKey2.upper).sorted
    (for {
      lowestDisjointLowerBound <- if (!equiv(rangeKey1.lower, rangeKey2.lower)) sortedRangeKeys.headOption else Option.empty[K]
      lowestDisjointUpperBound <- sortedRangeKeys.tail.headOption
      highestDisjointLowerBound <- sortedRangeKeys.tail.tail.headOption
      highestDisjointUpperBound <- if (!equiv(rangeKey1.upper, rangeKey2.upper)) sortedRangeKeys.lastOption else Option.empty[K]
    } yield {
      Set(
        RangeKey(lowestDisjointLowerBound, lowestDisjointUpperBound),
        RangeKey(highestDisjointLowerBound, highestDisjointUpperBound)
      )
    }).getOrElse(Set())
  }

}

object RangeTreeMap {

  def apply[K, V](implicit ordering : scala.Ordering[K]): RangeTreeMap[K, V] = new RangeTreeMap[K, V]

  def apply[K, V](initialMap: TreeMap[K, RangeEntry[K, V]])(implicit ordering : scala.Ordering[K]): RangeTreeMap[K, V] =
    new RangeTreeMap[K, V](Some(initialMap))

  def apply[K, V](initialMap: Map[K, RangeEntry[K, V]])(implicit ordering : scala.Ordering[K]): RangeTreeMap[K, V] =
    new RangeTreeMap[K, V](Some(new TreeMap() ++ initialMap))

  def equiv[K](value1: K, value2: K)(implicit ordering : scala.Ordering[K]): Boolean =
    ordering.equiv(value1, value2)

  def gteq[K](value1: K, value2: K)(implicit ordering : scala.Ordering[K]): Boolean =
    ordering.gteq(value1, value2)

  def lteq[K](value1: K, value2: K)(implicit ordering : scala.Ordering[K]): Boolean =
    ordering.lteq(value1, value2)

  def gt[K](value1: K, value2: K)(implicit ordering : scala.Ordering[K]): Boolean =
    ordering.gt(value1, value2)

  def lt[K](value1: K, value2: K)(implicit ordering : scala.Ordering[K]): Boolean =
    ordering.lt(value1, value2)
}