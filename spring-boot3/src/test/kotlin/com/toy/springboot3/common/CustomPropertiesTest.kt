package com.toy.springboot3.common

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CustomPropertiesTest @Autowired constructor(
  private val customProperties: CustomProperties
) {

  @Test
  fun myPropertiesTest() {
    println(customProperties)
  }

}