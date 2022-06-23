package com.example.ex.vo

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Test

internal class TestRequestVOTest {

  @Test
  fun `objectMapper 역직렬화` () {
    val testRequestVO = TestRequestVO("test")
    val json = ObjectMapper().writeValueAsString(testRequestVO)

    assertDoesNotThrow {
      val mapper = ObjectMapper()
      mapper.readValue(json, TestRequestVO::class.java)
    }
  }
}