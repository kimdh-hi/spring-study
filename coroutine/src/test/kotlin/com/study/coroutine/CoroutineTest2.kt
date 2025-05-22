package com.study.coroutine

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.time.measureTime

fun main() {
  val measureTime1 = runBlocking {
    measureTime {
      val job1 = async { sleepJob() }
      val job2 = async { sleepJob() }
      val job3 = async { sleepJob() }
      joinAll(job1, job2, job3)
    }
  }
  println("sleepJob ${measureTime1.inWholeMilliseconds}") // 3000ms

  val measureTime2 = runBlocking {
    measureTime {
      val job1 = async(Dispatchers.IO) { sleepJob() }
      val job2 = async(Dispatchers.IO) { sleepJob() }
      val job3 = async(Dispatchers.IO) { sleepJob() }
      joinAll(job1, job2, job3)
    }
  }
  println("sleepJob ${measureTime2.inWholeMilliseconds}") // 1000ms
}

private fun sleepJob(): String {
  Thread.sleep(1_000)
  println("thread=${Thread.currentThread().name}")
  return "result"
}
