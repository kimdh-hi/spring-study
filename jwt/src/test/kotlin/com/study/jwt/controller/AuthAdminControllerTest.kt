package com.study.jwt.controller

import com.study.jwt.base.AbstractIntegrationTest
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.get

internal class AuthAdminControllerTest(): AbstractIntegrationTest() {

    @Test
    fun `토큰 필요 api, 유효한 토큰`() {
        //when
        val result = mockMvc.get("/api/admin/hello") {
            header(HttpHeaders.AUTHORIZATION, "$BEARER_PREFIX$adminToken")
        }.andDo { print() }

        //then
        result.andExpect { status { isOk() } }
    }

    @Test
    fun `토큰인가 실패 - 사용자 권한으로 요청` () {
        //when
        val result = mockMvc.get("/api/admin/hello") {
            header(HttpHeaders.AUTHORIZATION, "$BEARER_PREFIX$userToken")
        }.andDo { print() }

        //then
        result.andExpect { status { HttpStatus.FORBIDDEN } }
    }
}