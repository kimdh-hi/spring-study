package com.toy.basic.`09-continue`

fun main() {
  val list = listOf(1,2,3,4,5,6,7,8,9,10)
  list.forEach point@ {
    if (it%2 == 0)
      return@point
    println(it)
  }
}