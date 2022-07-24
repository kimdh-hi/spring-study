package com.toy.`coroutines-basic`.`12-flow`

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

fun flow(): Flow<Int> = flow {
  for (i in 1..10) {
    delay(100)
    emit(i)
  }
}

fun main(): Unit = runBlocking {
  flow() // 콜드 스트림 - collect 전까지 실행되지 않는다.
  flow().collect{item -> println(item) }
}