package com.toy.`coroutines-basic`.`01-basic`

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun main() {
  // coroutine builder
  GlobalScope.launch {
    delay(1000L) // suspend function (일시중단)
    println("world")
  }

  println("hello")
  Thread.sleep(2000L) // thread blocking
}

//fun main() {
//  thread {
//    Thread.sleep(1000L)
//    println("world")
//  }
//  println("hello")
//  Thread.sleep(2000L)
//}