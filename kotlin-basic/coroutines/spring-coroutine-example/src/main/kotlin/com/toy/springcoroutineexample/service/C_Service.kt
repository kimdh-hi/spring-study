package com.toy.springcoroutineexample.service

import kotlinx.coroutines.delay
import org.springframework.stereotype.Service

@Service
class C_Service {

  suspend fun execute(): String {
    delay(1000L)
    return "C_Service_OK"
  }

  fun executeNormal(): String {
    Thread.sleep(1000L)
    return "C_Service_OK"
  }
}