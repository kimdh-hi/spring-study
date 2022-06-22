package com.toy.jpabasic.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.toy.jpabasic.repository.EmailAuthenticationRepository
import com.toy.jpabasic.repository.UserRepository
import com.toy.jpabasic.vo.UserSaveRequestVO
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
//@Transactional
internal class UserControllerTest(
  val mockMvc: MockMvc,
  val emailAuthenticationRepository: EmailAuthenticationRepository,
  val userRepository: UserRepository
) {

  @Test
  fun test() {
    //given
    val requestVO = UserSaveRequestVO(username = "error")

    //when
    val result = mockMvc.post("/api/users") {
      contentType = MediaType.APPLICATION_JSON
      content = ObjectMapper().writeValueAsString(requestVO)
    }

    //then
    assertTrue(emailAuthenticationRepository.findAll().size == 0)
    assertTrue(userRepository.findAll().size == 0)

    emailAuthenticationRepository.findAll().forEach {
      println("email-auth: ${it.id}")
    }

    userRepository.findAll().forEach {
      println("user: ${it.id}")
    }
  }
}