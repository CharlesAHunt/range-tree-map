# RangeTreeMap
> A generic map from ranges to values based on a TreeMap.

[![CircleCI](https://circleci.com/gh/CharlesAHunt/RangeTreeMap.svg?style=shield)](https://circleci.com/gh/CharlesAHunt/RangeTreeMap)
[![Latest version](https://index.scala-lang.org/charlesahunt/rangetreemap/range-tree-map/latest.svg?color=orange&v=1)](https://index.scala-lang.org/charlesahunt/rangetreemap/range-tree-map/0.1.3?target=_2.12)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.charlesahunt/range-tree-map_2.12/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.charlesahunt/range-tree-map_2.12)
[![Waffle.io - Columns and their card count](https://badge.waffle.io/CharlesAHunt/RangeTreeMap.svg?columns=all)](https://waffle.io/CharlesAHunt/RangeTreeMap)
[![codecov.io](http://codecov.io/github/charlesahunt/rangetreemap/coverage.svg?branch=master)](http://codecov.io/github/charlesahunt/rangetreemap?branch=master)

A supplemental Scala collection for mapping from ranges to values based on a TreeMap.

The underlying structure is a map from a lower bound to a RangeEntry.  If you have two ranges with the same lower bound,
the second range needs have a higher lower bound, ideally greater than or equal to the upper bound of the other range so
ranges don't overlap.

## Installation

sbt

```sh
"com.charlesahunt" %% "range-tree-map" % "0.1.3"
```

mill

```sh
ivy"com.charlesahunt::range-tree-map:0.1.3"
```

## Usage example


_For more examples and usage, please refer to the [Wiki](https://github.com/CharlesAHunt/RangeTreeMap/wiki)_


## Development setup

```scala

    import scala.math.Ordering._

    RangeTreeMap.apply[Long, Int]

```


## Release History

* 0.1.0
    * Work in progress

## Meta

Charles Hunt – [Website](http://cornfluence.com) – charlesalberthunt@gmail.com

Distributed under the MIT license. See ``LICENSE`` for more information.

## Contributing

1. Fork it (<https://github.com/charlesahunt/rangetreemap/fork>)
2. Create your feature branch (`git checkout -b feature/fooBar`)
3. Commit your changes (`git commit -am 'Add some fooBar'`)
4. Push to the branch (`git push origin feature/fooBar`)
5. Create a new Pull Request
