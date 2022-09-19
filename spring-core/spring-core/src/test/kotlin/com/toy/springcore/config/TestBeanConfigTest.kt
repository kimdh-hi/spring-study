package com.toy.springcore.config

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
internal class TestBeanConfigTest(
  private val testBeanService: TestBeanService
) {

  @Test
  fun test() {
    testBeanService.run()
  }
}