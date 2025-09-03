package com.study.resilience4j.controller

import com.study.resilience4j.httpclient.Test1Client
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test1")
class Test1Controller(
  private val test1Client: Test1Client,
) {

  @GetMapping("/1")
  fun test1(status: Int) = test1Client.test1(status)

  @GetMapping("/2")
  fun test2(status: Int) = test1Client.test2(status)
}

