package com.study.coroutine

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlin.time.measureTime

suspend fun main() {
  val delayedJobMeasureTime = measureTime {
    coroutineScope {
      val job1 = async { delayedJob() }
      val job2 = async { delayedJob() }
      val job3 = async { delayedJob() }

      println("job1: ${job1.await()}")
      println("job2: ${job2.await()}")
      println("job3: ${job3.await()}")
    }
  }
  println("delayedJob ${delayedJobMeasureTime.inWholeMilliseconds}") // 1000ms

  val sleepJobMeasureTime = measureTime {
    coroutineScope {
      val job1 = async { sleepJob() }
      val job2 = async { sleepJob() }
      val job3 = async { sleepJob() }

      println("job1: ${job1.await()}")
      println("job2: ${job2.await()}")
      println("job3: ${job3.await()}")
    }
  }
  println("sleepJob ${sleepJobMeasureTime.inWholeMilliseconds}") // 1000ms
}

private suspend fun delayedJob(): String {
  delay(1_000)
  return "result"
}

private fun sleepJob(): String {
  Thread.sleep(1_000)
  return "result"
}

