package com.lecture.snsapp.service

import com.lecture.snsapp.domain.Post
import com.lecture.snsapp.domain.User
import com.lecture.snsapp.exception.ApplicationException
import com.lecture.snsapp.exception.ErrorCode
import com.lecture.snsapp.repository.PostRepository
import com.lecture.snsapp.repository.UserRepository
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class PostServiceTest(
  private val postService: PostService
) {

  @MockkBean lateinit var postRepository: PostRepository
  @MockkBean lateinit var userRepository: UserRepository

  @Test
  fun `포스트 작성 성공`() {
    //given
    val title = "title"
    val body = "content"
    val username = "username"

    //when
    val user: User = mockk()
    val post: Post = mockk()
    every { userRepository.findByUsername(username) } returns user
    every { postRepository.save(any()) } returns post

    //then
    assertDoesNotThrow {
      postService.create(title, body, username)
    }
  }

  @Test
  fun `포스트 작성 실패 - 로그인하지 않은 사용자`() {
    //given
    val title = "title"
    val body = "content"
    val username = "username"

    //when
    val post: Post = mockk()
    every { userRepository.findByUsername(username) } returns null
    every { postRepository.save(any()) } returns post

    //then
    val exception = assertThrows<ApplicationException> {
      postService.create(title, body, username)
    }
    assertEquals(ErrorCode.USER_NOT_FOUND, exception.errorCode)
  }
}