package com.toy.`coroutines-basic`.`01-basic`

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
  GlobalScope.launch {
    delay(1000L)
    println("world")
  }
  println("hello")
  delay(2000L)
}