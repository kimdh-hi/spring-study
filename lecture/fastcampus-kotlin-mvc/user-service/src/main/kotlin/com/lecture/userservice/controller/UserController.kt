package com.lecture.userservice.controller

import com.lecture.userservice.config.annotation.AuthToken
import com.lecture.userservice.model.*
import com.lecture.userservice.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
class UserController(
  private val userService: UserService
) {

  @PostMapping("/signup")
  suspend fun signup(@RequestBody request: SignupRequest): ResponseEntity<SignupResponse> {
    val user = userService.signup(request)
    return ResponseEntity.ok(SignupResponse(user))
  }

  @PostMapping("/signin")
  suspend fun signIn(@RequestBody request: SignInRequest): ResponseEntity<SignInResponse> {
    val response = userService.signIn(request)
    return ResponseEntity.ok(response)
  }

  @PostMapping("/logout")
  suspend fun logout(@AuthToken token: String): ResponseEntity<Unit> {
    userService.logout(token)
    return ResponseEntity.ok().build()
  }

  @GetMapping("/me")
  suspend fun me(@AuthToken token: String): ResponseEntity<UserMeResponse> {
    val response = userService.getByToken(token)
    return ResponseEntity.ok(response)
  }
}