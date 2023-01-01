package com.lecture.cors2

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class Cors2Controller {

  @GetMapping("/users")
  fun users() = User("kim", 28)
}

data class User(
  val username: String,
  val age: Int
)