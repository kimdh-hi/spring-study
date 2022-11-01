package com.lecture.snsapp.controller

import com.lecture.snsapp.common.Response
import com.lecture.snsapp.service.UserService
import com.lecture.snsapp.vo.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
class UserController(
  private val userService: UserService
) {

  @PostMapping("/join")
  fun join(@RequestBody requestVO: UserJoinRequestVO): Response<UserResponseVO> {
    val responseVO = userService.join(requestVO.username, requestVO.password)
    return Response.success(result = responseVO)
  }

  @PostMapping("/login")
  fun login(@RequestBody requestVO: UserLoginRequestVO): Response<LoginResponseVO> {
    val token = userService.login(requestVO.username, requestVO.password)
    return Response.success(result = LoginResponseVO(token = token))
  }

  @GetMapping("/{username}")
  fun users(@PathVariable username: String) = userService.loadUserByUsername(username)

  @GetMapping("/alarm")
  fun getAlarm(pageable: Pageable, authentication: Authentication): ResponseEntity<Page<AlarmResponseVO>> {
    val user = authentication.principal as User
    val responseVO = userService.getAlarmList(user.username, pageable)
    return ResponseEntity.ok(responseVO)
  }
}