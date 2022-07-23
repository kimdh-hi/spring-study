package com.toy.emailauthentication.controller

import com.toy.emailauthentication.common.MailTemplates
import com.toy.emailauthentication.service.EmailAuthenticationService
import com.toy.emailauthentication.service.UserService
import com.toy.emailauthentication.vo.PasswordResetRequestVO
import com.toy.emailauthentication.vo.SendResetPasswordAuthenticationMailVO
import com.toy.emailauthentication.vo.SignupRequestVO
import com.toy.emailauthentication.vo.SignupResponseVO
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
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

  private val log = LoggerFactory.getLogger(javaClass)

  @PostMapping
  fun signup(@RequestBody requestVO: SignupRequestVO): ResponseEntity<SignupResponseVO> {
    val user = userService.signup(requestVO)
    emailAuthenticationService.sendMail(user.username, MailTemplates.SIGNUP)

    val responseVO = SignupResponseVO(username = user.username)
    return ResponseEntity.ok(responseVO)
  }

  @PostMapping("/send_reset_password_authentication_mail")
  fun sendResetPasswordAuthenticationMail(@RequestBody requestVO: SendResetPasswordAuthenticationMailVO): ResponseEntity<Unit> {
    emailAuthenticationService.sendMail(requestVO.username, MailTemplates.RESET_PASSWORD)

    return ResponseEntity.ok().build()
  }

  @PostMapping("/reset_password/{id}")
  fun resetPassword(@PathVariable id: String, @RequestBody requestVO: PasswordResetRequestVO) {
    log.info("reset id: {}", id)
    log.info("reset requestVO: {}", requestVO)
  }
}