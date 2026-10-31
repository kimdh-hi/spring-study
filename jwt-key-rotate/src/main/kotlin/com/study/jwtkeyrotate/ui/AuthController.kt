package com.study.jwtkeyrotate.ui

import com.study.jwtkeyrotate.application.AuthService
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
class AuthController(private val authService: AuthService) {
  @PostMapping("/login")
  fun login(@RequestParam username: String) = authService.login(username)

  @PostMapping("/refresh")
  fun refresh(@RequestParam refreshToken: String) =
    authService.refresh(refreshToken) ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED)

  @GetMapping("/me")
  fun me(@AuthenticationPrincipal jwt: Jwt) = MeResponse(jwt.headers["kid"] as String?)

  data class MeResponse(val kid: String?)
}
