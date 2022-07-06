package com.toy.basic


fun String.lastChar() = get(length-1)

fun main() {
  println("123".lastChar()) // -> 3
}

