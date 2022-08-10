package com.toy.basic.`08-collection-function`

fun main() {
  val list = listOf(1,2,3,4,5,6,7,8,9,10)

  println(list.filter { it % 2 == 0 })
}