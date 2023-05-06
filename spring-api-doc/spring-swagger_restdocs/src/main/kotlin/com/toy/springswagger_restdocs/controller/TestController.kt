package com.toy.springswagger_restdocs.controller

import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/test")
class TestController {

  @PostMapping
  fun save(@RequestBody dto: TestDto) = ResponseEntity.ok(dto)
}

data class TestDto(
  val data: String
)