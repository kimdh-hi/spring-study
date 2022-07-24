package com.toy.`coroutines-basic`.`10-withContext`

import kotlinx.coroutines.*

val exceptionHandler1 = CoroutineExceptionHandler { coroutineContext, throwable ->
  println("exceptionHandler")
}

suspend fun main(): Unit = coroutineScope {
  // withContext 에서 발생한 예외는 exceptionHandler에 걸리지 않는다.
  // 또한, catch 한 쪽에서 예외를 완전히 소화? 하고 coroutienScope로 전파하지 않는다.
  launch(exceptionHandler1) {
    try {
      withContext(Dispatchers.IO) {
        throw RuntimeException("ex...")
      }
    } catch (e: RuntimeException) {
      println("catch e...") // 예외는 여기서 catch
    }
  }
}