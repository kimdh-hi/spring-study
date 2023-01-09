package com.lecture.userservice.controller

import com.lecture.userservice.common.GreetingProperties
import com.lecture.userservice.service.UserService
import com.lecture.userservice.vo.UserSaveRequestVO
import com.lecture.userservice.vo.UserSaveResponseVO
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(
  private val greetingProperties: GreetingProperties,
  private val userService: UserService
) {

  @PostMapping
  fun save(@RequestBody vo: UserSaveRequestVO): ResponseEntity<UserSaveResponseVO> {
    val responseVO = userService.save(vo)
    return ResponseEntity.ok(responseVO)
  }

  @GetMapping("/health-check")
  fun healthCheck() = "user service ok"

  @GetMapping("/welcome")
  fun welcome() = greetingProperties.message
}