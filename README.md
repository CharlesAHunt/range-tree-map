# RangeTreeMap
> A generic map from ranges to values based on a TreeMap backed by Scala's red-black tree.

[![CircleCI](https://circleci.com/gh/CharlesAHunt/RangeTreeMap.svg?style=shield)](https://circleci.com/gh/CharlesAHunt/RangeTreeMap)
[![Maven Central](https://img.shields.io/maven-central/v/com.charlesahunt/range-tree-map_2.12.svg?color=green)](https://maven-badges.herokuapp.com/maven-central/com.charlesahunt/range-tree-map_2.12)
[![Waffle.io - Columns and their card count](https://badge.waffle.io/CharlesAHunt/RangeTreeMap.svg?columns=all)](https://waffle.io/CharlesAHunt/RangeTreeMap)
[![codecov.io](http://codecov.io/github/charlesahunt/rangetreemap/coverage.svg?branch=master)](http://codecov.io/github/charlesahunt/rangetreemap?branch=master)

The underlying data structure is a TreeMap backed by a Red-black tree mapping from a lower bound of `K` to a
 `RangeEntry[K, V]`, sorted by an implicit `Ordering[K]`.  Overlapping ranges are allowed with `put`, non overlapping
 ranges are handled using `putCoalesce`.

## Installation

sbt

```sh
"com.charlesahunt" %% "range-tree-map" % "0.3.1"
```

mill

```sh
ivy"com.charlesahunt::range-tree-map:0.3.1"
```

## Usage example

```scala

    import scala.math.Ordering._

    val rangeMap = RangeTreeMap.apply[Int, String]

    val rangeKey = RangeKey[Int](lower = 5, upper = 10)
    
    rangeMap.put(rangeKey, "exampleValue")

```
_For more examples and usage, please refer to the [Wiki](https://github.com/CharlesAHunt/RangeTreeMap/wiki)_


## Release History

* 0.3.1
    * Fix bug in putCoalesce with intersections on different values
* 0.3.0
    * Complete putCoalesce - First release with full functionality
* 0.2.3
    * Fix put to create discreet ranges, refactor putCoalesce which still needs to be completed
* 0.2.2
    * Fix bug with intersection which reverses the result lower and upper bound in the RangeKey
* 0.2.1
    * Rough completion of putCoalescing along with intersection and disjoint
* 0.1.5
    * Fix inclusive bugs in enclose and intersects functionality, add tests
* 0.1.4
    * Initial design and structuring, some basic functionality

## Meta

Charles Hunt – [Website](http://cornfluence.com) – charlesalberthunt@gmail.com

Distributed under the MIT license. See ``LICENSE`` for more information.
