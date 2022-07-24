package com.toy.kotlinjdsl.controller

import com.toy.kotlinjdsl.domain.User
import com.toy.kotlinjdsl.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/users")
@RestController
class UserController(
  private val userService: UserService
) {

  @GetMapping("/{id}")
  fun get(@PathVariable id: String): ResponseEntity<User> {
    val user = userService.get(id)
    return ResponseEntity.ok(user)
  }
}