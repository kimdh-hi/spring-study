package com.toy.`coroutines-basic`.basic

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread

// thread 보다 가볍고 빠르다
fun main() = runBlocking {
  repeat(100_000) {
    launch {
      delay(1000L)
      print(".")
    }
  }
}

//fun main() = runBlocking {
//  repeat(100_000) {
//    thread {
//      Thread.sleep(1000L)
//      print(".")
//    }
//  }
//}