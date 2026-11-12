package com.toy.springmfa.auth

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
  private val factorTokenService: FactorTokenService,
) {

  @PostMapping("/auth/login")
  fun login(@RequestBody request: LoginRequest): TokenResponse =
    TokenResponse(factorTokenService.login(request.username, request.password))

  data class LoginRequest(val username: String, val password: String)
}

data class TokenResponse(val accessToken: String)
