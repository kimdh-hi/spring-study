package com.toy.`coroutines-basic`.`07-coroutine-context-job`

import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
  println("CoroutineContext[Job]: ${coroutineContext[Job]}")

  launch {
    println("launch CoroutineContext[Job]: ${coroutineContext[Job]}")
  }

  async {
    println("async CoroutineContext[Job]: ${coroutineContext[Job]}")
  }
}