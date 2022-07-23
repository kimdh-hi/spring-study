package com.toy.`coroutines-basic`.`02-cancel`

import kotlinx.coroutines.*

/**
 * cancel 의 대상 job은 cancel에 협조적인 코드여야 한다.
 * 코루틴 내에서 suspend 함수가 호출되어야 한다. ex) delay() ...
 */

fun main() = runBlocking {
  val job = launch {
    repeat(10) { i ->
      println("launch job ... $i")
      Thread.sleep(500L)
//      delay(1L) // suspend 함수 호출
    }
  }

  delay(1300L)
  job.cancelAndJoin()
}
