package com.toy.jpafulltext.controller

import com.toy.jpafulltext.domain.User
import com.toy.jpafulltext.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(
  private val userService: UserService
) {

  @GetMapping("/v1/{description}")
  fun searchByDescription1(@PathVariable description: String): List<User> {
    return userService.searchByDescriptionV1(description)
  }

  @GetMapping("/v2/{description}")
  fun searchByDescription2(@PathVariable description: String): List<User> {
    return userService.searchByDescriptionV2(description)
  }
}