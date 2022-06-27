package com.toy.jpabasic.controller

import com.toy.jpabasic.repository.EmailAuthenticationRepository
import com.toy.jpabasic.repository.UserRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.jdbc.TestDatabaseAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Transactional
internal class UserControllerTest(
  val mockMvc: MockMvc,
  val emailAuthenticationRepository: EmailAuthenticationRepository,
  val userRepository: UserRepository
) {

  @Test
  fun authTest() {
    //given
    val authId = "auth-01"
    val targetUserId = "user-01"
    //when
    mockMvc.post("/api/users/authentication/$authId")

    //then
    val updatedUser = userRepository.findByIdOrNull(targetUserId)!!
    assertEquals("updateUsername", updatedUser.username)
  }

  @Test
  fun readOsivTest() {
    //given
    val userId = "user-01"

    //when
    mockMvc.get("/api/users/$userId")
  }

  @Test
  fun readOsivTest2() {
    //given
    val userId = "user-01"

    //when
    mockMvc.get("/api/users/$userId/company")
  }

//  @Test
//  fun test() {
//    //given
//    val requestVO = UserSaveRequestVO(username = "error")
//
//    //when
//    val result = mockMvc.post("/api/users") {
//      contentType = MediaType.APPLICATION_JSON
//      content = ObjectMapper().writeValueAsString(requestVO)
//    }
//
//    //then
//    assertTrue(emailAuthenticationRepository.findAll().size == 0)
//    assertTrue(userRepository.findAll().size == 0)
//
//    emailAuthenticationRepository.findAll().forEach {
//      println("email-auth: ${it.id}")
//    }
//
//    userRepository.findAll().forEach {
//      println("user: ${it.id}")
//    }
//  }


}