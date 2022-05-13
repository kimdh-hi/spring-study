package com.study.jwt.controller

import com.study.jwt.auth.JwtRequestVO
import com.study.jwt.auth.JwtResponseVO
import com.study.jwt.auth.JwtUtil
import com.study.jwt.service.AccountService
import com.study.jwt.vo.LoginRequestVO
import com.study.jwt.vo.SignupRequestVO
import com.study.jwt.vo.SignupResponseVO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/accounts")
@RestController
class AccountController(private val accountService: AccountService) {

    @PostMapping("/signup")
    fun signup(@RequestBody signupRequestVO: SignupRequestVO): ResponseEntity<SignupResponseVO> {
        val signupResponseVO = accountService.signup(signupRequestVO)
        return ResponseEntity.ok(signupResponseVO)
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequestVO: LoginRequestVO): ResponseEntity<JwtResponseVO> {
        val account = accountService.authenticate(loginRequestVO.username, loginRequestVO.password)
        val token = JwtUtil.generateToken(JwtRequestVO.fromAccount(account))
        return ResponseEntity.ok(JwtResponseVO.newInstance(token))
    }
}