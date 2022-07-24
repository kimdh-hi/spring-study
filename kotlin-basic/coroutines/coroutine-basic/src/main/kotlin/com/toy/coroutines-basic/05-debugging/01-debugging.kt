package com.toy.`coroutines-basic`.`05-debugging`

import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

/**
 * JVM Option: -Dkotlinx.coroutines.debug
 *
 * 어떤 쓰레드의 `어떤 코루틴` 에서 실행된 것인지
 * */
fun log(message: String)
  = println("[${Thread.currentThread().name}] : $message")

fun main() = runBlocking {
  val a = async {
    log("log for a ...")
    10
  }

  val b = async {
    log("log for b ...")
    50
  }
  log("answer: ${a.await()}, ${b.await()}")
}
