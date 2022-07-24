package com.toy.`coroutines-basic`.`01-basic`

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
  val job = GlobalScope.launch {
    delay(3000L)
    println("world")
  }

  println("hello")
  job.join() // job 에 할당된 코루틴이 종료될 때까지 대기
}