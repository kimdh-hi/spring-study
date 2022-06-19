package com.toy.`coroutines-basic`.`03-suspend-function`

import kotlinx.coroutines.CoroutineStart.LAZY
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

/**
 * CoroutineStart.LAZY
 * start() or await() 시 async 내 코루틴을 실행
 *
 * c+b -> a+b
 */

fun main() = runBlocking {
  val time = measureTimeMillis {
    // 아래 라인을 지날 대 코루틴을 실행하지 않음 ( trigger: start() or await() )
    val a = async(start = LAZY) { doSomethingA() }
    val b = async(start = LAZY) { doSomethingB() }

    val c = async { doSomethingC() }
    val d = async { doSomethingD() }

    println("c + d = ${c.await() + d.await()}")

    a.start()
    b.start()
    // start 가 호출되지 않았다면 LAZY 코루틴 내에서 호출된 두 suspend 함수는 동기적으로 호출될 것임
    // a를 실행시키고 await 를 통해 값을 받은 후 b를 실행시키고 값을 받을 것이기 때문
    println("a + b = ${a.await() + b.await()}")
  }
  println("spend time: $time")
}


suspend fun doSomethingC(): Int {
  println("call C")
  delay(1000L)
  return 10
}

suspend fun doSomethingD(): Int {
  println("call D")
  delay(1500L)
  return 15
}