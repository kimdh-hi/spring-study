package com.toy.springmvc.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.toy.springmvc.common.ErrorCodes
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.multipart
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class ErrorSerializerControllerTest(
  private val mockMvc: MockMvc,
  private val objectMapper: ObjectMapper
) {

  @Test
  fun requestBodyTest() {
    //given
    val vo = ErrorSerializerTest1VO(data1 = " ", email = "asd")

    //when
    val result = mockMvc.post("/api/error-serializer/json") {
      contentType = MediaType.APPLICATION_JSON
      content = objectMapper.writeValueAsString(vo)
    }
      .andDo { print() }

    //then
    result.andExpect {
      jsonPath("$.errorCode") { value(ErrorCodes.INVALID_PARAMETER) }
    }
  }

  @Test
  fun modelAttributeTest() {
    //given
    val vo = ErrorSerializerTest1VO(data1 = " ", email = "asd")

    //when
    val result = mockMvc.multipart("/api/error-serializer/form") {
      param("data1", vo.data1)
      param("email", vo.email)
    }
      .andDo { print() }

    //then
    result.andExpect {
      jsonPath("$.errorCode") { value(ErrorCodes.INVALID_PARAMETER) }
    }
  }
}