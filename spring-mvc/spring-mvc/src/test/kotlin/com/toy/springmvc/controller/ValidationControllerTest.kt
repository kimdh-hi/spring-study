package com.toy.springmvc.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.*
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
internal class ValidationControllerTest(
  private val mockMvc: MockMvc,
  private val objectMapper: ObjectMapper
) {
  @Test
  fun test() {
    val requestVO = ValidationDataVO(names = listOf("name", " "))

    mockMvc.post("/api/validation") {
      contentType = MediaType.APPLICATION_JSON
      content = objectMapper.writeValueAsString(requestVO)
    }
      .andDo { print() }
      .andExpect {
        jsonPath("$.errorCode") { value("9002") }
      }
  }
}