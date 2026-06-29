package com.study.archunit.user.ui

import com.study.archunit.user.application.UserService
import com.study.archunit.user.domain.User
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
  private val userService: UserService,
) {

  @GetMapping
  fun test(): ResponseEntity<Unit> {
    val user = userService.findAll()
    return ResponseEntity.ok().build()
  }

  @GetMapping
  fun test2(): ResponseEntity<Unit> {
    val user: User? = userService.getOneOrNull()
    return ResponseEntity.ok().build()
  }
}

