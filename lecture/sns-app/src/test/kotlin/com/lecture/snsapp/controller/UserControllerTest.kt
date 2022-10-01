package com.lecture.snsapp.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.lecture.snsapp.vo.UserJoinRequestVO
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class UserControllerTest(
  private val mockMvc: MockMvc,
  private val objectMapper: ObjectMapper
) {

  @Test
  fun `회원가입 성공`() {
    //given
    val username = "username"
    val password = "password"
    val requestVO = UserJoinRequestVO(username = username, password = password)

    //expect
    mockMvc.post("/api/v1/users/join") {
      contentType = MediaType.APPLICATION_JSON
      content = objectMapper.writeValueAsString(requestVO)
    }
      .andDo { print() }
      .andExpect { status { isOk() } }
  }

  @Test
  fun `회원가입 실패 - 이미 가입된 username`() {
    //given
    val username = "username"
    val password = "password"
    val requestVO = UserJoinRequestVO(username = username, password = password)

    //expect
    mockMvc.post("/api/v1/users/join") {
      contentType = MediaType.APPLICATION_JSON
      content = objectMapper.writeValueAsString(requestVO)
    }
      .andDo { print() }
      .andExpect { status { isConflict() } }
  }

}