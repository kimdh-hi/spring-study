package com.toy.`coroutines-basic`.`10-withContext`

import kotlinx.coroutines.*

suspend fun main(): Unit = coroutineScope {
  launch {
    withContext(Dispatchers.IO) {
      func1()
    }
  }
  println("main...")
}

suspend fun func1() {
  delay(3000L)
  println("func1...")
}