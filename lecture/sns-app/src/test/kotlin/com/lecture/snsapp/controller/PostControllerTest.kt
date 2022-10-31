package com.lecture.snsapp.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.lecture.snsapp.repository.UserRepository
import com.lecture.snsapp.service.PostService
import com.lecture.snsapp.vo.CommentRequestVO
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
import org.springframework.test.web.servlet.*
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Transactional
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
  @WithMockUser(username = "test2-username")
  fun `포스트 수정 실패 - 작성자가 아닌 경우`() {
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
  @WithMockUser(username = "test-username")
  fun `포스트 삭제`() {
    mockMvc.delete("/api/v1/posts/post-01")
      .andDo { print() }
      .andExpect { status { isOk() } }
  }

  @Test
  @WithAnonymousUser
  fun `포스트 삭제 실패 - 로그인하지 않은 사용자`() {
    mockMvc.delete("/api/v1/posts/post-01")
      .andExpect {  }
      .andExpect { status { is4xxClientError() } }
  }

  @Test
  @WithMockUser(username = "test2-username")
  fun `포스트 삭제 실패 - 작성자가 아닌 경우`() {
    mockMvc.delete("/api/v1/posts/post-01")
      .andExpect {  }
      .andExpect { status { is4xxClientError() } }
  }

  @Test
  @WithMockUser(username = "test-username")
  fun `포스트 삭제 실패 - 포스트가 존재하지 않는 경우`() {
    mockMvc.delete("/api/v1/posts/not-exists")
      .andExpect {  }
      .andExpect { status { is4xxClientError() } }
  }

  @Test
  @WithMockUser(username = "test-username")
  fun `포스트 목록`() {
    mockMvc.get("/api/v1/posts")
      .andDo { print() }
      .andExpect {
        status { isOk() }
      }
  }

  @Test
  @WithAnonymousUser
  fun `포스트 목록 실패 - 로그인하지 않은 경우`() {
    mockMvc.get("/api/v1/posts")
      .andDo { print() }
      .andExpect {
        status { is4xxClientError() }
      }
  }

  @Test
  @WithMockUser(username = "test-username")
  fun `내 포스트 목록`() {
    mockMvc.get("/api/v1/posts/me")
      .andDo { print() }
      .andExpect {
        status { isOk() }
      }
  }

  @Test
  @WithAnonymousUser
  fun `내 포스트 목록 실패 - 로그인하지 않은 경우`() {
    mockMvc.get("/api/v1/posts/me")
      .andDo { print() }
      .andExpect {
        status { is4xxClientError() }
      }
  }

  @Test
  @WithMockUser(username = "test-username")
  fun `포스트 좋아요`() {
    mockMvc.post("/api/v1/posts/post-02/likes")
      .andDo { print() }
      .andExpect {
        status { isOk() }
      }
  }

  @Test
  @WithMockUser(username = "test-username")
  fun `포스트 좋아요 실패 - 게시글이 없는 경우`() {
    mockMvc.post("/api/v1/posts/post-not-exists/likes")
      .andDo { print() }
      .andExpect {
        status { is4xxClientError() }
      }
  }

  @Test
  @WithMockUser(username = "test2-username")
  fun `포스트 좋아요 실패 - 이미 좋아요가 눌린 게시물`() {
    mockMvc.post("/api/v1/posts/post-02/likes")
      .andDo { print() }
      .andExpect {
        status { is4xxClientError() }
      }
  }

  @Test
  @WithAnonymousUser
  fun `포스트 좋아요 실패 - 로그인하지 않은 경우`() {
    mockMvc.post("/api/v1/posts/post-01/likes")
      .andDo { print() }
      .andExpect {
        status { is4xxClientError() }
      }
  }

  @Test
  @WithMockUser(username = "test-username")
  fun `포스트 좋아요 개수`() {
    mockMvc.get("/api/v1/posts/post-02/likes/count")
      .andDo { print() }
      .andExpect {
        status { isOk() }
        jsonPath("$.result") { value(2) }
      }
  }

  @Test
  @WithMockUser(username = "test-username")
  fun `포스트 댓글달기`() {
    mockMvc.post("/api/v1/posts/post-01/comments") {
      contentType = MediaType.APPLICATION_JSON
      content = objectMapper.writeValueAsString(CommentRequestVO("test-comment1"))
    }
      .andDo { print() }
      .andExpect {
        status { isOk() }
      }
  }

  @Test
  @WithMockUser(username = "test-username")
  fun `포스트 댓글조회`() {
    mockMvc.get("/api/v1/posts/post-01/comments") {
      param("page", "0")
      param("size", "3")
    }
      .andDo { print() }
      .andExpect {
        status { isOk() }
        jsonPath("$.result.content") { isNotEmpty() }
      }
  }
}