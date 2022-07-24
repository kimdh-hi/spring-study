package com.toy.`coroutines-basic`.`02-cancel`

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
  val job = launch {
    repeat(1000) { i ->
      println("launch job ... $i")
      delay(500L)
    }
  }

  delay(1300L)
  job.cancel()
  job.join()
}
