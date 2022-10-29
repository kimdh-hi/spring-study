package com.lecture.snsapp.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.lecture.snsapp.repository.UserRepository
import com.lecture.snsapp.service.PostService
import com.lecture.snsapp.vo.PostCreateRequestVO
import com.lecture.snsapp.vo.PostModifyRequestVO
import com.ninjasquad.springmockk.MockkBean
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put

@SpringBootTest
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class PostControllerTest(
  private val mockMvc: MockMvc,
  private val objectMapper: ObjectMapper,
) {

  @Test
  @WithMockUser(username = "test-username")
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
      .andExpect { status { is4xxClientError() } }
  }

  @Test
  @WithMockUser(username = "test-username")
  fun `포스트 수정`() {
    //given
    val title = "title"
    val body = "content"
    val requestVO = PostModifyRequestVO(title = title, body = body)

    //then
    mockMvc.put("/api/v1/posts/post-01") {
      contentType = MediaType.APPLICATION_JSON
      content = objectMapper.writeValueAsString(requestVO)
    }
      .andDo { print() }
      .andExpect { status { isOk() } }
  }

  @Test
  @WithAnonymousUser
  fun `포스트 수정 실패 - 로그인하지 않은 사용자`() {
    //given
    val title = "title"
    val body = "content"
    val requestVO = PostModifyRequestVO(title = title, body = body)

    //then
    mockMvc.put("/api/v1/posts") {
      contentType = MediaType.APPLICATION_JSON
      content = objectMapper.writeValueAsString(requestVO)
    }
      .andDo { print() }
      .andExpect { status { is4xxClientError() } }
  }

  @Test
  @WithMockUser
  fun `포스트 수정 실패 - 작성자가 아닌 사용자가 수정`() {
    //given
    val title = "title"
    val body = "content"
    val requestVO = PostModifyRequestVO(title = title, body = body)

    //then
    mockMvc.post("/api/v1/posts") {
      contentType = MediaType.APPLICATION_JSON
      content = objectMapper.writeValueAsString(requestVO)
    }
      .andDo { print() }
      .andExpect { status { is4xxClientError() } }
  }
}