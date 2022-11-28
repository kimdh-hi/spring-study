package com.toy.basic.`14-when`

fun main() {

  val test = WhenTestEnum.CCC

  when(test) {
    WhenTestEnum.AAA -> {
      println("aaa")
    }
    WhenTestEnum.BBB, WhenTestEnum.CCC -> {
      println("bbb or ccc")
    }
  }
}

enum class WhenTestEnum {
  AAA, BBB, CCC
}