package com.toy.serviceb

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ServiceAClientTest @Autowired constructor(
  private val serviceAClient: ServiceAClient
) {

  @Test
  fun test() {
    //when
    val result = serviceAClient.test("data")

    //then
    val dto = result.body as TestDto
    assertEquals("data", dto.data)
  }
}