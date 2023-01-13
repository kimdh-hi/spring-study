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
  fun test1() {
    val requestVO = ValidationDataVO(names = listOf("name", " "), nickname = "nick", email = "asd@gmail.com")

    mockMvc.post("/api/validation") {
      contentType = MediaType.APPLICATION_JSON
      content = objectMapper.writeValueAsString(requestVO)
    }
      .andDo { print() }
      .andExpect {
        jsonPath("$.errorCode") { value("9002") }
      }
  }

  @Test
  fun test2() {
    val requestVO = ValidationDataVO(names = listOf("name", "nam2"), nickname = " ", email = "asd@gmail.com")

    mockMvc.post("/api/validation") {
      contentType = MediaType.APPLICATION_JSON
      content = objectMapper.writeValueAsString(requestVO)
    }
      .andDo { print() }
      .andExpect {
        jsonPath("$.errorCode") { value("9002") }
      }
  }

  @Test
  fun test3() {
    val requestVO = ValidationDataVO(names = listOf("name", "nam2"), nickname = "nick", email = "asdasdasd")

    mockMvc.post("/api/validation") {
      contentType = MediaType.APPLICATION_JSON
      content = objectMapper.writeValueAsString(requestVO)
    }
      .andDo { print() }
      .andExpect {
        jsonPath("$.errorCode") { value("9002") }
      }
  }

  @Test
  fun test4() {
    val requestVO = ValidationDataVO(names = listOf("name", "nam2"), nickname = "nick", email = " ")

    mockMvc.post("/api/validation") {
      contentType = MediaType.APPLICATION_JSON
      content = objectMapper.writeValueAsString(requestVO)
    }
      .andDo { print() }
      .andExpect {
        jsonPath("$.errorCode") { value("9002") }
      }
  }

  @Test
  fun test5() {
    val requestVO = ValidationData2VO(email = "asd")

    mockMvc.post("/api/validation/test") {
      contentType = MediaType.APPLICATION_JSON
      content = objectMapper.writeValueAsString(requestVO)
    }
      .andDo { print() }
      .andExpect {
        jsonPath("$.errorCode") { value("9002") }
      }
  }
}


