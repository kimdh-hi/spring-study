package com.toy.`coroutines-basic`.`03-suspend-function`

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main() {
  try {
    val time = measureTimeMillis {
      val a = somethingA_Async()
      val b = somethingB_Async()

      throw RuntimeException("exception occurred...") // 예외발생

      runBlocking {
        println("result: ${a.await() + b.await()}")
      }
    }
    println("spend time: $time")
  } catch (ex: RuntimeException) {
    println("catch exception ...")
  }

  runBlocking {
    delay(10000000L)
  }
}

// 안티패턴: async 함수를 재사용 목적으로 GlobalScope 로 만든 것. (Async-style function)
// 아래 함수는 suspend function 도 아님.. 일반 함수 어디서든 호출 가능
// 또한 아래 함수를 호출할 곳에서 예외가 발생한 경우 somethingA_Async 는 예외와 전혀 관계없이 자기 동작을 해버림
fun somethingA_Async() =  GlobalScope.async {
  println("somethingA_Async start")
  val res = funcA()
  println("somethingA_Async end")
  res
}
fun somethingB_Async() = GlobalScope.async {
  println("somethingB_Async start")
  val res = funcB()
  println("somethingB_Async end")
  res
}

suspend fun funcA(): Int {
  println("call A")
  delay(3000L)
  return 10
}

suspend fun funcB(): Int {
  println("call B")
  delay(3000L)
  return 15
}