package com.toy.reactivejdsl.controller

import com.toy.reactivejdsl.service.UserService
import com.toy.reactivejdsl.vo.LoginRequestVO
import com.toy.reactivejdsl.vo.LoginResponseVO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/login")
@RestController
class LoginController(private val userService: UserService) {

  @PostMapping
  suspend fun login(@RequestBody requestVO: LoginRequestVO): ResponseEntity<LoginResponseVO> {
    val token = userService.login(requestVO)
    val responseVO = LoginResponseVO(token)
    return ResponseEntity.ok(responseVO)
  }
}