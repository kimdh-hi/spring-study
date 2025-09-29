package com.study.testcontainer.ui

import com.study.testcontainer.application.UserService
import com.study.testcontainer.domain.User
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(
  private val userService: UserService,
) {

  @PostMapping
  fun save(@RequestBody request: UserSaveRequest): ResponseEntity<String> {
    val id = userService.save(request)
    return ResponseEntity.ok(id)
  }

  @GetMapping("/{userId}")
  fun get(@PathVariable userId: String): ResponseEntity<UserResponse> {
    val response = userService.get(userId)
    return ResponseEntity.ok(response)
  }
}

data class UserSaveRequest(val name: String) {
  fun toEntity() = User.of(name)
}

data class UserResponse(val id: String = "", val name: String = "")
