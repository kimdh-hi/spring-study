package com.toy.basic.`15-map-sort`

fun main() {
  val map = mapOf("a" to 1, "c" to 3, "b" to 2)
  val sortedMap = map.toList().sortedByDescending { it.second }.toMap()

  println(sortedMap)
}