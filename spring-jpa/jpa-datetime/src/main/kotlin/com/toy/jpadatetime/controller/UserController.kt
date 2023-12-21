package com.toy.jpadatetime.controller

import com.toy.jpadatetime.domain.User
import com.toy.jpadatetime.service.UserService
import org.springframework.http.ResponseEntity
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
  fun save(@RequestBody request: UserSaveRequest): ResponseEntity<User> {
    val user = userService.save(request)
    return ResponseEntity.ok(user)
  }
}

data class UserSaveRequest(
  val name: String
) {
  fun toEntity() = User(
    name = name
  )
}