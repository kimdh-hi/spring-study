package com.toy.springwebclient.controller

import com.toy.springwebclient.service.TestService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class TestController(
  private val testService: TestService
) {

  @GetMapping
  fun test()
    = ResponseEntity.ok(testService.test())

}