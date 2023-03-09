package com.toy.springgradle

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class Test @Autowired constructor(
  private val testProperties: TestProperties
) {

  @Test
  fun test() {
    println(testProperties)
  }
}