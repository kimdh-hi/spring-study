package com.toy.reactivejdsl.controller

import com.toy.reactivejdsl.vo.SignupRequestVO
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/account")
class AccountController {

  @PostMapping("/signup")
  fun signup(@RequestBody requestVO: SignupRequestVO) {

  }
}