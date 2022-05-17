package com.study.jwt.controller

import com.study.jwt.auth.JwtPrincipal
import com.study.jwt.auth.JwtUtil
import com.study.jwt.service.AccountService
import com.study.jwt.vo.LoginRequestVO
import com.study.jwt.vo.LoginResponseVO
import com.study.jwt.vo.SignupRequestVO
import com.study.jwt.vo.SignupResponseVO
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
    fun login(@RequestBody loginRequestVO: LoginRequestVO): ResponseEntity<LoginResponseVO> {
        val account = accountService.authenticate(loginRequestVO.username, loginRequestVO.password)

        val loginResponseVO: LoginResponseVO = if (!account.hasExpiredPasswordUpdateDate()) { // 기한 만료 X
            val token = JwtUtil.generateToken(JwtPrincipal.fromAccount(account))
            LoginResponseVO(token, passwordExpired = false)
        } else if (account.hasExpiredPasswordUpdateDate() && loginRequestVO.passwordUpdateLater) { // 기한 만료 & 나중에 변경
            accountService.extendPasswordUpdateDate(account)
            val token = JwtUtil.generateToken(JwtPrincipal.fromAccount(account))
            LoginResponseVO(token, passwordExpired = false)
        } else { // 기한 만료 & 지금 변경
            LoginResponseVO(passwordExpired = true)
        }

        return ResponseEntity.ok(loginResponseVO)
    }
}