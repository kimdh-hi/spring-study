package com.toy.springaop.service

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class TestServiceTest @Autowired constructor(
  private val testService: TestService
) {

  @Test
  fun test() {
    testService.logic()
  }
}