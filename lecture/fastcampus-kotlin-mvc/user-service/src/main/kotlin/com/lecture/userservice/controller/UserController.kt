package com.lecture.userservice.controller

import com.lecture.userservice.model.SignupRequest
import com.lecture.userservice.model.SignupResponse
import com.lecture.userservice.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
class UserController(
  private val userService: UserService
) {

  @PostMapping
  suspend fun signup(@RequestBody request: SignupRequest): ResponseEntity<SignupResponse> {
    val user = userService.signup(request)
    return ResponseEntity.ok(SignupResponse(user))
  }
}