package com.toy.oauthbasic.controller

import com.toy.oauthbasic.service.UserService
import com.toy.oauthbasic.utils.SecurityUtils
import com.toy.oauthbasic.vo.UserMeResponseVO
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(
  private val userService: UserService
) {

  @GetMapping("/me")
  @PreAuthorize("isAuthenticated()")
  fun me(): ResponseEntity<UserMeResponseVO> {
    val principal = SecurityUtils.getPrincipal()
    val user = userService.get(principal.userId)
    return ResponseEntity.ok(UserMeResponseVO.of(user))
  }
}