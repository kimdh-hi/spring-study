package com.toy.`coroutines-basic`.`02-cancel`

import kotlinx.coroutines.*

/**
 * 코루틴 내에서 suspend 함수를 호출하지 않고 cancel 에 협조작인 코드
 * isActive 사용
 *
 * 이전 suspend 함수로 cancel 시키는 방식은 내부적으로 예외를 통해 동자을 멈추는 방식임
 * isActive 의 경우 예외가 아닌 자신의 상태값으로 cancel 여부를 체크함
 */

fun main() = runBlocking {
  val job = launch(Dispatchers.Default) {
    repeat(10) { i ->
      while(isActive) {
        println("launch job ... $i")
        Thread.sleep(500L)
      }
    }
  }

  delay(1300L)
  job.cancelAndJoin()
}