package com.toy.`coroutines-basic`.`01-basic`

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
  // GlobalScope XX
  // runBlocking scope 를 사용 - 새로운 부모 Scope를 생성하지 않는 것?
  // 알아서 자신의 자식 코루틴? 이 종료되는 것을 대기
  launch { // 코루틴의 반환값이 없는 경우 사용 (있는 경우 async)
    delay(1000L)
    println("world")
  }
  println("hello")
}