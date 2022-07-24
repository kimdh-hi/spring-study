package com.toy.`coroutines-basic`.`02-cancel`

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull

/**
 * withTimeoutOrNull
 *
 * timeout 발샛시 예외가 아닌 null을 반환
 * 코루틴 내 모든 작업이 지정한 시간 내에 종료된다면 리턴값을 반환
 */

fun main() = runBlocking {

  val repeatTime = 10

  val result = withTimeoutOrNull(1300L) {
    repeat(repeatTime) { i ->
      println("launch job ... $i")
      delay(500L)
    }
    "Success"
  }

  println("result: $result")
}