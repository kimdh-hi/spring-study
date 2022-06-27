package com.toy.`coroutines-basic`.`08-coroutine-hierarchy`

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {

  val request = launch {

    val request = launch { // parents coroutine ...

      GlobalScope.launch { // inside to parents.. but not affected from parents when parents be canceled
        println("GlobalScope Job ... running ...")
        delay(1000)
        println("GlobalScope Job ... not affected from cancel")
      }

      launch { // child coroutine.. .
        delay(100)
        println("Child coroutine Job ... running ...")
        delay(1000)
        println("cancel by parent coroutine ... this line should be not executed ...")
      }
    }

    delay(500)
    request.cancel() // cancel parent coroutine
    delay(1000)

  }
}