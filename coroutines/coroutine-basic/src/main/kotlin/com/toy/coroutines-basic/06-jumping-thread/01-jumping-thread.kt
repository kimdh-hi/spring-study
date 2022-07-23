package com.toy.`coroutines-basic`.`06-jumping-thread`

import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

fun log(message: String)
  = println("[${Thread.currentThread().name}] : $message")

/**
 * 중간에 withContext를 이용해서 명시적으로 다른 쓰레드에서 코루틴을 실행시킴.
 * 결과는 순서대로 나옴 wow
 */

fun main() {
  newSingleThreadContext("ctx1").use { ctx1 ->
    newSingleThreadContext("ctx2").use { ctx2 ->
        runBlocking(ctx1) {
          log("ctx1 start ...")
          withContext(ctx2) { // thread 전환
            log("ctx2 processing ...")
          }
          log("comeback to ctx1 ...")
      }
    }
  }
}
