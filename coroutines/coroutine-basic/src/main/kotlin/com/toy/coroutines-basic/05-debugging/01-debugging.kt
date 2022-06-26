package com.toy.`coroutines-basic`.`05-debugging`

import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

/**
 * VM Option: -Dkotlinx.coroutines.debug
 */

fun log(message: String)
  = println("thread-name: ${Thread.currentThread().name}, $message")

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
