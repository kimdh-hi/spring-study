package com.toy.springmvc.controller

import com.fasterxml.jackson.databind.ObjectMapper
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
internal class EventControllerTest(
  private val mockMvc: MockMvc,
  private val objectMapper: ObjectMapper
) {

  @Test
  fun saveByModelAttributes() {
    mockMvc.post("/events/model-attributes") {
      param("name", "event-name")
      param("limitOfEnrollment", "1000")
    }
      .andDo { print() }
      .andExpect {
        status { isOk() }
        jsonPath("$.name") { value("event-name") }
        jsonPath("$.limitOfEnrollment") { value(1000) }
      }
  }

  @Test
  fun saveByModelAttributesInvalidParam() {
    mockMvc.post("/events/model-attributes") {
      param("name", " ")
      param("limitOfEnrollment", "-1")
    }
      .andDo { print() }
      .andExpect {
        status { isOk() }
      }
  }
}
