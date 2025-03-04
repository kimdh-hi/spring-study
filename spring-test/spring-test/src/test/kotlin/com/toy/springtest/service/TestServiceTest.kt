package com.toy.springtest.service

import com.toy.springtest.base.AbstractTest
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class TestServiceTest : AbstractTest() {

  @Test
  fun test() {
    every { testComponent.logic() } returns "logic"
    testService.logic()
  }
}
