package com.study.jwt.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.study.jwt.base.TestData
import com.study.jwt.repository.AccountRepository
import com.study.jwt.vo.LoginRequestVO
import com.study.jwt.vo.SignupRequestVO
import com.study.jwt.vo.SignupResponseVO
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.transaction.annotation.Transactional

@Transactional
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AutoConfigureMockMvc
@SpringBootTest
internal class AccountControllerTest(
    val objectMapper: ObjectMapper, val mockMvc: MockMvc,
    val accountRepository: AccountRepository) {

    @DisplayName("회원가입 테스트")
    @Nested
    inner class SignupTest {

        @Test
        fun `회원가입 성공`() {
            //given
            val signupRequestVO = SignupRequestVO("testUser", "testPassword")

            //when
            val result = mockMvc.post("/accounts/signup") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(signupRequestVO)
            }

            //then
            result.andExpect { status { isOk() } }
            val account = accountRepository.findByUsername("testUser")
            assertAll({
                assertNotNull(account)
                assertEquals(TestData.ROLE_USER_ID, account?.role?.id)
            })
        }
    }

    @DisplayName("username / password 로그인 테스트")
    @Nested
    inner class LoginTest {

        @Test
        fun `로그인 성공 - 토큰 발급`() {
            //given
            val loginRequestVO =
                LoginRequestVO(username = TestData.ACCOUNT_ADMIN_USERNAME, password = TestData.ACCOUNT_ADMIN_PASSWORD)

            //when
            val result = mockMvc.post("/accounts/login") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(loginRequestVO)
            }.andDo { print() }

            //then
            result.andExpect {
                status { isOk() }
                jsonPath("$.token") { exists() }
            }
        }
    }

    @DisplayName("회원가입 후 토큰발급 테스트")
    @Nested
    inner class SignupLoginTest {
        @Test
        fun `로그인 성공 - 토큰 발급`() {
            //given
            val username = "testUser"
            val password = "testUser"
            val signupRequestVO = SignupRequestVO(username, password)

            //when
            val signupResult = mockMvc.post("/accounts/signup") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(signupRequestVO)
            }

            //then
            signupResult.andExpect { status { isOk() } }
            val account = accountRepository.findByUsername(username)
            assertAll({
                assertNotNull(account)
                assertEquals(TestData.ROLE_USER_ID, account?.role?.id)
            })

            //given
            val loginRequestVO =
                LoginRequestVO(username = username, password = password)

            //when
            val loginResult = mockMvc.post("/accounts/login") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(loginRequestVO)
            }.andDo { print() }

            //then
            loginResult.andExpect {
                status { isOk() }
                jsonPath("$.token") { exists() }
            }
        }
    }
}