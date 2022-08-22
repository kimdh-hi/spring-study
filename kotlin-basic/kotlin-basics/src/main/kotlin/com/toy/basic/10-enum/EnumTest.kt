package com.toy.basic.`10-enum`

fun main() {
  println(Idp.valueOf("google".uppercase()))
}

enum class Idp {
  GOOGLE, NAVER
}