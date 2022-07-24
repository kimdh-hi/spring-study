package com.toy.`coroutines-basic`.`12-flow`

import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
  flowOf(list)
    .map { it + 1 }
    .collect{ println(it) }
}