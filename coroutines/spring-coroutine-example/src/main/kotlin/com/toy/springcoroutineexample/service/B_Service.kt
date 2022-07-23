package com.toy.springcoroutineexample.service

import kotlinx.coroutines.delay
import org.springframework.stereotype.Service

@Service
class B_Service {

  suspend fun execute(): String {
    delay(1000L)
    return "B_Service_OK"
  }

  fun executeNormal(): String {
    Thread.sleep(1000L)
    return "B_Service_OK"
  }
}