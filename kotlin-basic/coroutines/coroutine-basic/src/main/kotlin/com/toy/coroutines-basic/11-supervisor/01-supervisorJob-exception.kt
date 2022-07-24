package com.toy.`coroutines-basic`.`11-supervisor`

import kotlinx.coroutines.*

suspend fun main() {
  val coroutineContext = Dispatchers.IO + SupervisorJob()

  CoroutineScope(Dispatchers.IO).launch {

    val firstChildJob = launch(coroutineContext) {
      throw RuntimeException("ex...")
    }

    val secondChildJob = launch(Dispatchers.IO) {
      delay(1000L)
      println("second job is alive")
    }

    firstChildJob.join()
    secondChildJob.join()
  }.join()
}