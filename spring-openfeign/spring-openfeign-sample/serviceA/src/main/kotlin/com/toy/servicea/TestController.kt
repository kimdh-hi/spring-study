package com.toy.servicea

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/serviceA/test")
class TestController {

  @GetMapping
  fun test(dto: TestDto) = ResponseEntity.ok(dto)
}

data class TestDto(
  val data: String
)