package com.toy.`coroutines-basic`.`03-suspend-function`

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main() = runBlocking {

  /**
   * non-blocking 이지만 순차성을 보장한다. doSomethingA -> doSomethingB
   */
  val time = measureTimeMillis {
    val a = doSomethingA()
    val b = doSomethingB()
    println("a + b = ${a+b}")
  }
  println("spend time: $time")
}

suspend fun doSomethingA(): Int {
  println("call A")
  delay(1000L)
  return 10
}

suspend fun doSomethingB(): Int {
  println("call B")
  delay(1500L)
  return 15
}
