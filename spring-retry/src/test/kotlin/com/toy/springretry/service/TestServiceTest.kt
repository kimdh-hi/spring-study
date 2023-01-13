package com.toy.springretry.service

import com.toy.springretry.exceptions.CustomException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
internal class TestServiceTest(
  private val testService: TestService
) {

  @Test
  fun test() {
    assertThrows<CustomException> {
      testService.exceptionTest()
    }
  }
}