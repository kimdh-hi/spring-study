package com.toy.webfluxr2dbcpostgres.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/auth")
@RestController
class AuthController {

  @GetMapping
  fun auth() = "ok"
}