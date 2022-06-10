package com.toy.`coroutines-basic`.basic

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
  launch {
    world()
  }
  println("hello")
}

suspend fun world() {
  delay(1000L)
  println("world")
}