package com.charlesahunt

import scala.collection.mutable

/**
  * A data structure backed by a TreeMap where numeric ranges map to values and where the key is the lower bound.
  *
  * @tparam K some number type
  * @tparam V any value
  */
class TreeRangeMap[K, V] {

  val rangeMap = new mutable.TreeMap[K, RangeEntry[K, V]]

  def asDescendingMapOfRanges(): Map[Range[K], V] = {}

  def asMapOfRanges(): Map[Range[K], V] = {}

  def clear(): Unit = rangeMap.clear

  def get(key: K): V = {}

  def put(range: Range[K], value: V): Unit = {}

  def putAll(rangeMap: TreeRangeMap[K, V]): Unit = {}

  def putCoalescing(range: Range[K] , value: V): Unit = {}

  def remove(rangeToRemove: Range[K]): Unit = rangeMap.remove(rangeToRemove.lower)

  def span(): Range[K] = {}

  def subRangeMap(subRange: Range[K]): TreeRangeMap[K,V] = {}

}

object TreeRangeMap {
  def apply[K, V]() = new TreeRangeMap[K, V]
}

case class Range[K](lower: K, upper: K)

case class RangeEntry[K, V]()