package com.lecture.snsapp.controller

import com.lecture.snsapp.common.Response
import com.lecture.snsapp.service.UserService
import com.lecture.snsapp.vo.LoginResponseVO
import com.lecture.snsapp.vo.UserJoinRequestVO
import com.lecture.snsapp.vo.UserLoginRequestVO
import com.lecture.snsapp.vo.UserResponseVO
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
}