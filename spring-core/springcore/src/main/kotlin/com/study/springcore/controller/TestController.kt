package com.study.springcore.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class TestController {

  @GetMapping
  fun get() = ResponseEntity.ok(TestResponse("ok!"))

  @PostMapping
  fun post(@RequestBody request: TestRequest) = ResponseEntity.ok(TestResponse(request.data))
}

data class TestRequest(
  val data: String,
)

data class TestResponse(val message: String)
