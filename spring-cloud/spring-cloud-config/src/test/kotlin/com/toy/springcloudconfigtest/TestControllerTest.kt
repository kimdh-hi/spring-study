package com.toy.springcloudconfigtest

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest(
  properties = ["spring.cloud.config.enabled=true", "management.endpoints.web.exposure.include=*", "encrypt.key=abcd1234"]
)
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class TestControllerTest(
  private val mockMvc: MockMvc
) {

  @Test
  fun test() {
    mockMvc.get("/api/test/enc/v2")
      .andDo { print() }
  }

}