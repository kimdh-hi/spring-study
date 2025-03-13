package com.toy.springkotlin.controller

import com.toy.springkotlin.controller.dto.UserSaveRequest
import com.toy.springkotlin.controller.dto.UserSaveV1Request
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController {

  private val log = LoggerFactory.getLogger(UserController::class.java)

  @PostMapping("/v1")
  fun saveV1(@RequestBody @Valid request: UserSaveV1Request): ResponseEntity<Unit> {
    log.debug("userSaveRequest={}", request)
    return ResponseEntity.ok().build()
  }

  @PostMapping("/v2")
  fun saveV2(@RequestBody @Valid request: UserSaveRequest): ResponseEntity<Unit> {
    log.debug("userSaveRequest={}", request)
    return ResponseEntity.ok().build()
  }

  @GetMapping("path-variables/{userTestId}")
  fun pathVariableTest(@PathVariable userTestId: UserTestId) = ResponseEntity.ok(userTestId)
}

@JvmInline
value class UserTestId(
  val value: String,
) {
  override fun toString(): String = value
}
