package com.toy.`coroutines-basic`.`09-coroutine-scope`

import kotlinx.coroutines.*

suspend fun main() = coroutineScope {
  launch { coroutineFunc2() }
  println("im main...")
}

//suspend fun main() = coroutineScope {
//  val result = async { coroutineFunc3() }
//  println("im main")
//  println("result: ${result.await()}")
//}

suspend fun coroutineFunc2() {
  delay(3000L)
  println("im coroutineFunc...")
}

suspend fun coroutineFunc3(): String {
  delay(3000L)
  println("im coroutineFunc...")
  return "ok"
}