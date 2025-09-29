package com.study.testcontainer.ui

import com.study.testcontainer.base.IntegrationTest
import com.study.testcontainer.domain.User
import com.study.testcontainer.domain.UserRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

class UserControllerTest @Autowired constructor(
  private val userRepository: UserRepository,
) : IntegrationTest() {

  @Test
  fun saveUser() {
    val request = UserSaveRequest("test")
    mockMvc.post("/users") {
      contentType = MediaType.APPLICATION_JSON
      content = objectMapper.writeValueAsString(request)
    }.andExpectAll {
      status { isOk() }
      jsonPath("$") { isNotEmpty() }
    }
  }

  @Test
  fun get() {
    val user = userRepository.save(User.of("test1"))
    mockMvc.get("/users/{userId}", user.id!!)
    mockMvc.get("/users/{userId}", user.id!!)
  }
}
