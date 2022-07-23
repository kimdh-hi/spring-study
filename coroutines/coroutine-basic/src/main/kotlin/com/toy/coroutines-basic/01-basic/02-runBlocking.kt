package com.toy.`coroutines-basic`.`01-basic`

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
  GlobalScope.launch {
    delay(1000L)
    println("world")
  }

  println("hello")
  runBlocking { // 자신을 호출한 Thread 를 blocking 하는 코루틴
    delay(2000L)
  }
}