package com.toy.webfluxr2dbcpostgres.controller

import com.toy.webfluxr2dbcpostgres.service.UserService
import com.toy.webfluxr2dbcpostgres.vo.LoginRequestVO
import com.toy.webfluxr2dbcpostgres.vo.LoginResponseVO
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