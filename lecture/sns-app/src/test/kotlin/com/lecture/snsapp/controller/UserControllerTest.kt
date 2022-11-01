package com.lecture.snsapp.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.lecture.snsapp.domain.User
import com.lecture.snsapp.exception.ApplicationException
import com.lecture.snsapp.exception.ErrorCode
import com.lecture.snsapp.service.UserService
import com.lecture.snsapp.vo.UserJoinRequestVO
import com.lecture.snsapp.vo.UserLoginRequestVO
import com.lecture.snsapp.vo.UserResponseVO
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkClass
import io.mockk.mockkObject
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Transactional
class UserControllerTest(
  private val mockMvc: MockMvc,
  private val objectMapper: ObjectMapper
) {

  @MockkBean
  lateinit var userService: UserService

  @Test
  fun `회원가입`() {
    //given
    val username = "username"
    val password = "password"
    val requestVO = UserJoinRequestVO(username = username, password = password)
    val responseVO = UserResponseVO(id = "user-01", username = username)

    //when
    every { userService.join(username, password) } returns responseVO

    //then
    mockMvc.post("/api/v1/users/join") {
      contentType = MediaType.APPLICATION_JSON
      content = objectMapper.writeValueAsString(requestVO)
    }
      .andDo { print() }
      .andExpect { status { isOk() } }
  }

  @Test
  fun `회원가입 실패 - 중복된 username`() {
    //given
    val username = "username"
    val password = "password"
    val requestVO = UserJoinRequestVO(username = username, password = password)
    every { userService.join(username, password) }.throws(ApplicationException(ErrorCode.DUPLICATED_USER_NAME))

    // expect
    mockMvc.post("/api/v1/users/join") {
      contentType = MediaType.APPLICATION_JSON
      content = objectMapper.writeValueAsString(requestVO)
    }
      .andDo { print() }
      .andExpect { status { is4xxClientError() } }
  }

  @Test
  fun `로그인`() {
    //given
    val username = "username"
    val password = "password"
    val requestVO = UserLoginRequestVO(username = username, password = password)
    every { userService.login(requestVO.username, requestVO.password) } returns "token"

    // expect
    mockMvc.post("/api/v1/users/login") {
      contentType = MediaType.APPLICATION_JSON
      content = objectMapper.writeValueAsString(requestVO)
    }
      .andDo { print() }
      .andExpect { status { isOk() } }
  }

  @Test
  fun `로그인 실패 - 회원가입 되지 않은 username`() {
    //given
    val username = "username"
    val password = "password"
    val requestVO = UserLoginRequestVO(username = username, password = password)
    every { userService.login(requestVO.username, requestVO.password) } throws ApplicationException(ErrorCode.DUPLICATED_USER_NAME)

    // expect
    mockMvc.post("/api/v1/users/login") {
      contentType = MediaType.APPLICATION_JSON
      content = objectMapper.writeValueAsString(requestVO)
    }
      .andDo { print() }
      .andExpect { status { is4xxClientError() } }
  }

  @Test
  fun `로그인 실패 - 올바르지 않은 password`() {
    //given
    val username = "username"
    val password = "password"
    val requestVO = UserLoginRequestVO(username = username, password = password)
    every { userService.login(requestVO.username, requestVO.password) } throws ApplicationException(ErrorCode.DUPLICATED_USER_NAME)

    // expect
    mockMvc.post("/api/v1/users/login") {
      contentType = MediaType.APPLICATION_JSON
      content = objectMapper.writeValueAsString(requestVO)
    }
      .andDo { print() }
      .andExpect { status { is4xxClientError() } }
  }

  @Test
  @WithMockUser
  fun `알람`() {
    mockMvc.get("/api/v1/users/alarm")
      .andDo { print() }
      .andExpect {
        status { isOk() }
      }
  }
}