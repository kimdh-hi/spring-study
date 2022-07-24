package com.toy.`coroutines-basic`.`02-cancel`

import kotlinx.coroutines.*

/**
 * withTimeout + suspend function
 *
 * timeout 시 TimeoutCancellationException 발생
 */

fun main() = runBlocking {
  withTimeout(1300L) {
    repeat(10) { i ->
        println("launch job ... $i")
        delay(500L)
    }
  }
}