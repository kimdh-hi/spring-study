package com.study.springcore.movkmvctester

import tools.jackson.databind.ObjectMapper
import com.study.springcore.controller.TestRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.boot.webmvc.test.autoconfigure.MockMvcPrint
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.assertj.MockMvcTester
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc(print = MockMvcPrint.SYSTEM_OUT, printOnlyOnFailure = false)
class MockMvcTesterTest @Autowired constructor(
  private val mockMvcTester: MockMvcTester,
  private val mockMvc: MockMvc,
  private val objectMapper: ObjectMapper,
) {

  @Test
  fun get() {
    val result1 = mockMvcTester.get()
      .uri("/test")
      .exchange()
    assertThat(result1)
      .hasStatus(HttpStatus.OK)
      .bodyJson()
      .extractingPath("$.message").isEqualTo("ok!")
  }

  @Test
  fun get2() {
    val result1 = mockMvcTester.get()
      .uri("/test")
      .exchange()
    assertThat(result1)
      .hasStatus(HttpStatus.OK)
      .bodyJson()
      .extractingPath("$.message").isEqualTo("ok!")
  }

  @Test
  fun post() {
    val request = TestRequest("data")
    val result = mockMvcTester.post()
      .uri("/test")
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(request))
      .exchange()

    assertThat(result)
      .hasStatus(HttpStatus.OK)
      .bodyJson()
      .extractingPath("$.message").isEqualTo(request.data)
  }
}
