package com.toy.`coroutines-basic`.`10-withContext`

import kotlinx.coroutines.*

val exceptionHandler2 = CoroutineExceptionHandler { coroutineContext, throwable ->
  println("exceptionHandler2")
}


suspend fun main(): Unit = coroutineScope {
  launch(exceptionHandler2) {
    try {
      async(Dispatchers.IO) {
        throw RuntimeException("ex...")
      }.await()
    } catch (e: RuntimeException) {
      println("catch e...")
    }
  }
}