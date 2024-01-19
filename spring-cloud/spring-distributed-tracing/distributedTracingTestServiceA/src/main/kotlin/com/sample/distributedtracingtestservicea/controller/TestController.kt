package com.sample.distributedtracingtestservicea.controller

import com.sample.distributedtracingtestservicea.service.TestService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sample")
class TestController(
  private val testService: TestService
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @GetMapping("/test")
  fun test(): String {
    log.info("test..")

    return "test"
  }

  @PostMapping("/save")
  fun save(@RequestBody request: SampleSaveRequest): String {
    testService.save(request)
    return "ok"
  }
}

data class SampleSaveRequest(
  val data: String,
  val bData: String
)