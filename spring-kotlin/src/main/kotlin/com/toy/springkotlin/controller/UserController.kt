package com.toy.springkotlin.controller

import com.toy.springkotlin.controller.dto.UserSaveRequest
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController {

  private val log = LoggerFactory.getLogger(UserController::class.java)

  @PostMapping
  fun save(@RequestBody request: UserSaveRequest): ResponseEntity<Unit> {
    log.debug("userSaveRequest={}", request)
    return ResponseEntity.ok().build()
  }
}
