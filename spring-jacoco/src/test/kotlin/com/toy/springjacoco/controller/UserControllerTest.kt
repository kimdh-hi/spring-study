package com.toy.springjacoco.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AutoConfigureMockMvc
class UserControllerTest(
  private val mockMvc: MockMvc,
  private val objectMapper: ObjectMapper,
) {

  @Test
  fun create() {
    mockMvc.post("/users") {
      contentType = MediaType.APPLICATION_JSON
      content = objectMapper.writeValueAsString(UserCreateRequest("name"))
    }
      .andExpect { status { isOk() } }
  }
}
