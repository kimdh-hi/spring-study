package com.toy.springredisevent.controller

import com.toy.springredisevent.user.domain.UserStatus
import com.toy.springredisevent.user.service.UserService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(
  private val userService: UserService
) {

  @PutMapping("/{userId}/status")
  fun updateStatus(@PathVariable userId: String, @RequestBody vo: UserStatusUpdateRequestVO) {
    userService.updateStatus(userId, vo)
  }
}

data class UserStatusUpdateRequestVO(
  val statusName: String,
  val iconPath: String
) {
  fun toUserStatus() = UserStatus(
    statusName = statusName,
    statusIconPath = iconPath
  )
}