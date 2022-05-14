package com.study.jwt.controller

import com.study.jwt.auth.JwtPrincipal
import com.study.jwt.auth.JwtUtil
import com.study.jwt.base.AbstractIntegrationTest
import com.study.jwt.base.TestData
import com.study.jwt.repository.AccountRepository
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

internal class AuthUserControllerTest (): AbstractIntegrationTest() {

    @Test
    fun `토큰인증 성공 - user - userApi`() {
        //given
        val user = accountRepository.findById(TestData.ACCOUNT_USER_ID).get()
        val token = JwtUtil.generateToken(JwtPrincipal.fromAccount(user))

        //when
        val result = mockMvc.get("/api/user/hello") {
            header(HttpHeaders.AUTHORIZATION, "$BEARER_PREFIX$token")
        }.andDo { print() }

        //then
        result.andExpect { status { isOk() } }
    }

    @Test
    fun `토큰인증 성공 - admin - userApi`() {
        //given
        val admin = accountRepository.findById(TestData.ACCOUNT_ADMIN_ID).get()
        val token = JwtUtil.generateToken(JwtPrincipal.fromAccount(admin))

        //when
        val result = mockMvc.get("/api/user/hello") {
            header(HttpHeaders.AUTHORIZATION, "$BEARER_PREFIX$token")
        }.andDo { print() }

        //then
        result.andExpect { status { isOk() } }
    }

}