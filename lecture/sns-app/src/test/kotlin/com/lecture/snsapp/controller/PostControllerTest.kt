package com.lecture.snsapp.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.lecture.snsapp.vo.PostCreateRequestVO
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class PostControllerTest(
  private val mockMvc: MockMvc,
  private val objectMapper: ObjectMapper
) {

  @Test
  @WithMockUser
  fun `포스트 작성`() {
    //given
    val title = "title"
    val body = "content"
    val requestVO = PostCreateRequestVO(title = title, body = body)

    //then
    mockMvc.post("/api/v1/posts") {
      contentType = MediaType.APPLICATION_JSON
      content = objectMapper.writeValueAsString(requestVO)
    }
      .andDo { print() }
      .andExpect { status { isOk() } }
  }

  @Test
  @WithAnonymousUser
  fun `포스트 작성 실패 - 로그인하지 않은 사용자`() {
    //given
    val title = "title"
    val body = "content"
    val requestVO = PostCreateRequestVO(title = title, body = body)

    //then
    mockMvc.post("/api/v1/posts") {
      contentType = MediaType.APPLICATION_JSON
      content = objectMapper.writeValueAsString(requestVO)
    }
      .andDo { print() }
      .andExpect { status { isOk() } }
  }
}