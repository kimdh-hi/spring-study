package com.study.springgraalvm.ui

import com.study.springgraalvm.application.UserService
import com.study.springgraalvm.domain.model.User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/companies/{companyId}/users")
class UserController(
  private val userService: UserService,
) {
  @PostMapping
  fun create(
    @PathVariable companyId: Long,
    @RequestBody request: CreateUserRequest,
  ): UserResponse =
    UserResponse.from(userService.create(companyId, request.name, request.email))

  @GetMapping
  fun findByCompany(@PathVariable companyId: Long): List<UserResponse> =
    userService.findByCompany(companyId).map(UserResponse::from)
}

data class CreateUserRequest(val name: String, val email: String)

data class UserResponse(val id: Long, val name: String, val email: String, val companyId: Long) {
  companion object {
    fun from(user: User) = UserResponse(user.id, user.name, user.email, user.company.id)
  }
}
