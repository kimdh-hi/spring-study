package com.study.jwt.controller

import com.study.jwt.auth.JwtPrincipal
import com.study.jwt.auth.JwtUtil
import com.study.jwt.service.AccountService
import com.study.jwt.util.SecurityUtil
import com.study.jwt.vo.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/accounts")
@RestController
class AccountController(private val accountService: AccountService) {

    @PostMapping("/signup")
    fun signup(@RequestBody signupRequestVO: SignupRequestVO): ResponseEntity<SignupResponseVO> {
        val signupResponseVO = accountService.signup(signupRequestVO)
        return ResponseEntity.ok(signupResponseVO)
    }

    @PostMapping("/login")
    fun login(@RequestBody requestVO: LoginRequestVO): ResponseEntity<LoginResponseVO> {
        val account = accountService.authenticate(requestVO.username, requestVO.password)
        val token = JwtUtil.generateToken(JwtPrincipal.fromAccount(account))
        val loginResponseVO = LoginResponseVO(token = token, passwordExpired = false)

        if (account.hasExpiredPasswordUpdateDate()) {
            loginResponseVO.passwordExpired = true
        }

        return ResponseEntity.ok(loginResponseVO)
    }

    @PutMapping("/password")
    fun updatePassword(@RequestBody requestVO: PasswordUpdateRequestVO) {
        val jwtPrincipal = SecurityUtil.getCurrentPrincipal()

    }

    @PutMapping("/password/extends-update-date")
    fun extendsPasswordUpdateDate() {
        val jwtPrincipal = SecurityUtil.getCurrentPrincipal()
    }

    //  @PostMapping(UrlConstants.LOGIN)
//  fun login(
//    @RequestBody @Valid requestVO: LoginRequestVO,
//    result: BindingResult
//  ): ResponseEntity<LoginResponseVO> {
//    if (result.hasErrors())
//      throw ParameterException(result)
//
//    val user = authenticationService.authenticate(requestVO.username, requestVO.password)
//
//    val hasExpiredPasswordUpdateDate = user.hasExpiredPasswordUpdateDate()
//
//    val responseVO = if (!hasExpiredPasswordUpdateDate) {
//      val token = jwtUtil.generateToken(JwtPrincipal.of(user))
//      LoginResponseVO(token = token, passwordUpdateDateExpired = false)
//    } else if (requestVO.passwordUpdateLater) {
//      user.extendsPasswordUpdateDate()
//      val token = jwtUtil.generateToken(JwtPrincipal.of(user))
//      LoginResponseVO(token = token, passwordUpdateDateExpired = false)
//    } else {
//      LoginResponseVO(token = "", passwordUpdateDateExpired = true)
//    }
//
//    return ResponseEntity.ok(responseVO)
//  }
}