package com.toy.springr2dbc.controller

import com.toy.springr2dbc.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(
  private val userService: UserService
) {

  @GetMapping("/jpa")
  fun findAllByJpa() = userService.findAllByJpa()

  @GetMapping("/r2dbc")
  suspend fun findAllByR2dbc() = userService.findAllByR2dbc()
}