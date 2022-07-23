package com.study.jwt.controller

import com.study.jwt.base.AbstractIntegrationTest
import com.study.jwt.base.SecurityConstants
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

internal class PublicControllerTest(): AbstractIntegrationTest() {

    @Test
    fun `인증을 필요로 하지 않는 api에 토큰 없이 접근`() {
        mockMvc.get("/api/public")
            .andExpect { status { isOk() } }
    }

    /*
    현재 SpringSecurity 설정은 모든 요청에 대해 열어두고 메서드 Security를 사용해서 api에 대한 보안을 적용하고 있음
    인증을 필요로 하지 않는 요청에 토큰이 포함된 경우에도 토큰을 파싱하는 등의 처리가 이루어 짐.
    ajax 의 글로벌 설정에서 모든 ajax 요청에 토큰을 포함하도록 하는 경우가 있는데 이런 경우 불필요한 작업이 빈번하게 발생할 듯
     */
    @Test
    fun `인증을 필요로 하지 않은 api에 토큰을 포함하고 접근`() {
        mockMvc.get("/api/public") {
            header(HttpHeaders.AUTHORIZATION, "${SecurityConstants.BEARER_TYPE_PREFIX}${userToken}")
        }.andExpect { status { isOk() } }
    }
}