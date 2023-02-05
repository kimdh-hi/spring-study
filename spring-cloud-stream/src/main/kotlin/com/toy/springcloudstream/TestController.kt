package com.toy.springcloudstream

import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/test")
class TestController(
  private val streamBridge: StreamBridge
) {

  @PostMapping("/message/{message}")
  fun send(@PathVariable message: String): String {

    return "ok"
  }

}