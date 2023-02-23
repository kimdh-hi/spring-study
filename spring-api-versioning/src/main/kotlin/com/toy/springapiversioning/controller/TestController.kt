package com.toy.springapiversioning.controller

import com.toy.springapiversioning.aop.ApiVersion
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
@ApiVersion("1")
class UserController {

  @GetMapping
  fun usersV1() = "usersV1"

  @GetMapping
  @ApiVersion("1.1")
  fun usersV1_1() = "usersV1.1"

  @GetMapping
  @ApiVersion("2")
  fun usersV2() = "usersV2"
}