package com.toy.jpabasic.controller

import com.toy.jpabasic.domain.User
import com.toy.jpabasic.service.UserService
import com.toy.jpabasic.vo.UserSaveRequestVO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(
  private val userService: UserService
) {

  @PostMapping
  fun save(@RequestBody requestVO: UserSaveRequestVO): ResponseEntity<User> {
    val savedUser = userService.save(requestVO)

    return ResponseEntity.ok(savedUser)
  }
}