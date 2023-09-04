package com.toy.springmvc.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.toy.springmvc.domain.Person
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
internal class MessageConverterControllerTest(
  private val mockMvc: MockMvc,
  private val objectMapper: ObjectMapper,
) {

  @Test
  fun defaultStringMessageConverter() {
    mockMvc.get("/message-converter/message") {
      content = "message"
    }
      .andDo { print() }
      .andExpect {
        content { string("message") }
      }
  }

  @Test
  fun jsonMessageConverter() {
    val person = Person(name = "person")
    mockMvc.get("/message-converter/json-message") {
      contentType = MediaType.APPLICATION_JSON
      content = objectMapper.writeValueAsString(person)
    }
      .andDo { print() }
      .andExpect {
        jsonPath("$.name") { value("person") }
      }
  }
}