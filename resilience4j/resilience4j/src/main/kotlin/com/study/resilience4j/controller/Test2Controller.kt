package com.study.resilience4j.controller

import com.study.resilience4j.httpclient.Test2Client
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test2")
class Test2Controller(
  private val test2Client: Test2Client,
) {

  @GetMapping("/1")
  fun test1(status: Int) = test2Client.test1(status)

  @GetMapping("/2")
  fun test2(status: Int) = test2Client.test2(status)
}

