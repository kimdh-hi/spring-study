package com.toy.basic.`02-scope-function`

fun main() {
  val list = listOf(
    TestClass("a1", "a2"),
    TestClass("b1", "b2"),
    TestClass("c1", "c2"),
    TestClass("d1", "d2"),
  )

  with(list) {
    println("this: $this")
    println("size: $size")
  }
}