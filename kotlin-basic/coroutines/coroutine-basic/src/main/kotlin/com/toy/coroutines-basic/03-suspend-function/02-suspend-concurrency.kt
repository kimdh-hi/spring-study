package com.toy.`coroutines-basic`.`03-suspend-function`

import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main() = runBlocking {

  /**
   *  doSomethingA 와 doSomethingB 의 의존성이 없는 경우 비동기 논블록킹 형태가 가능
  */
  val time = measureTimeMillis {
    val a = async { doSomethingA() } // 1000ms
    val b = async { doSomethingB() } // 1500ms
    val result = a.await() + b.await()
    println("a + b = $result")
  }
  println("spend time: $time")
}
