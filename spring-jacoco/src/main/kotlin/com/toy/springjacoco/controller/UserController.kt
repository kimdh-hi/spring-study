package com.toy.springjacoco.controller

import com.toy.springjacoco.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(
  private val userService: UserService
) {

  @PostMapping
  fun create(@RequestBody request: UserCreateRequest): ResponseEntity<String> {
    val user = userService.create(request.name)
    return ResponseEntity.ok(user.id)
  }
}

data class UserCreateRequest(
  val name: String
)
