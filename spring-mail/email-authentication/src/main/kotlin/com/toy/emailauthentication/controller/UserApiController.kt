package com.toy.emailauthentication.controller

import com.toy.emailauthentication.common.MailTemplates
import com.toy.emailauthentication.service.EmailAuthenticationService
import com.toy.emailauthentication.service.UserService
import com.toy.emailauthentication.vo.SignupRequestVO
import com.toy.emailauthentication.vo.SignupResponseVO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserApiController(
  private val userService: UserService,
  private val emailAuthenticationService: EmailAuthenticationService
) {

  @PostMapping
  fun signup(@RequestBody requestVO: SignupRequestVO): ResponseEntity<SignupResponseVO> {
    val user = userService.signup(requestVO)
    emailAuthenticationService.sendMail(user, MailTemplates.SIGNUP)

    val responseVO = SignupResponseVO(username = user.username)
    return ResponseEntity.ok(responseVO)
  }
}