package com.toy.`coroutines-basic`.`12-flow`

import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking

val list = listOf(1,2,3,4,5,6,7,8,9,10)

fun main() = runBlocking {

  flowOf(*(list.toTypedArray()))
    .collect{item -> println(item) }

  println("============================")

  list.asFlow().collect{item -> println(item) }

}