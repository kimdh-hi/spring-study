package com.toy.springmfa.ui

import com.toy.springmfa.application.LoginService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class LoginController(
  private val loginService: LoginService,
) {

  @PostMapping("/login")
  fun login(@RequestBody request: LoginRequest): LoginService.LoginResult =
    loginService.login(request.username, request.password)

  data class LoginRequest(val username: String, val password: String)
}

data class TokenResponse(val accessToken: String)
