package com.toy.`coroutines-basic`.`03-suspend-function`

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

fun main() = runBlocking {
  try {
    val time = measureTimeMillis {
      println("result: ${sumFromTwoSuspendFunction()}")
    }

    println("spend time: $time")
  } catch (ex: RuntimeException) {
    println("catch exception ...")
  }
}

/**
 * structured concurrency style
 */
// GlobalScope.async 로 만들어진 함수와 다르게 suspend 함수이다. 즉 코루틴 내에서만 호출 가능하다.
suspend fun sumFromTwoSuspendFunction() = coroutineScope {
  val a = async { funcA() }
  val b = async { funcB() }

  // 스코프 내 앞서 실행된 코루틴들은 cancel 된다.
  throw RuntimeException("exception occurred...")

  a.await() + b.await()
}