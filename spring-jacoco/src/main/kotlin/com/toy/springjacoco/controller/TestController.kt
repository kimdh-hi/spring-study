package com.toy.springjacoco.controller

import com.toy.springjacoco.service.TestService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class TestController(
  private val testService: TestService
) {

  @GetMapping
  fun test(): ResponseEntity<String> {
    testService.test()
    return ResponseEntity.ok("ok")
  }

  @PostMapping
  fun test2(): ResponseEntity<String> {
    return ResponseEntity.ok("ok")
  }
}
