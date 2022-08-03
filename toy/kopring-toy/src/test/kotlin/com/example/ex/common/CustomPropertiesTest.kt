package com.example.ex.common

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
internal class CustomPropertiesTest(
  val customProperties: CustomProperties
) {

  @Test
  fun customPropertiesTest() {
    println(customProperties.imageExtensions)

    println(customProperties.fileExtensions)
  }
}