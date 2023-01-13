package com.toy.springmvc.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import javax.validation.Valid
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

@RestController
@RequestMapping("/api/error-serializer")
class ErrorSerializerController {

  @PostMapping("/json")
  fun requestBodyTest(@RequestBody @Valid vo: ErrorSerializerTest1VO): ResponseEntity<String> {
    return ResponseEntity.ok("ok")
  }

  @PostMapping("/form")
  fun modelAttributeTest(@ModelAttribute @Valid vo: ErrorSerializerTest2VO): ResponseEntity<String> {
    return ResponseEntity.ok("ok")
  }
}

data class ErrorSerializerTest1VO(
  @field:NotBlank
  val data1: String,

  @field:Email
  val email: String,
)

data class ErrorSerializerTest2VO(
  @field:NotBlank
  val data1: String,

  @field:Email
  val email: String,

  val multipartFile: MultipartFile? = null
)