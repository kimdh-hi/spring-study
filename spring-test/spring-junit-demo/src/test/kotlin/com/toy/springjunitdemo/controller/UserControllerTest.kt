package com.toy.springjunitdemo.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.put
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AutoConfigureMockMvc
@Transactional
internal class UserControllerTest(
  private val mockMvc: MockMvc,
  private val objectMapper: ObjectMapper
) {

  @Test
  fun delete() {
    mockMvc.delete("/api/users/1")
      .andDo { print() }
      .andExpect { status { isOk() } }
  }

  @Test
  fun get() {
    mockMvc.get("/api/users/1")
      .andDo { print() }
      .andExpect { status { isOk() } }
  }

  @Test
  fun list() {
    mockMvc.get("/api/users")
      .andDo { print() }
      .andExpect { status { isOk() } }
  }

  @Test
  fun update() {
    val requestVO = UserRequestVO(username = "update")

    mockMvc.put("/api/users/1") {
      contentType = MediaType.APPLICATION_JSON
      content = objectMapper.writeValueAsString(requestVO)
    }
      .andDo { print() }
      .andExpect { status { isOk() } }
  }
}