package com.toy.springthymeleaf.message

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.MessageSource
import java.util.*

@SpringBootTest
class MessageSourceTest @Autowired constructor(
  private val messageSource: MessageSource
) {

  @Test
  fun test1() {
    val message = messageSource.getMessage("hello", null, "default", Locale.KOREA)
    assertEquals("hello", message)
  }

  @Test
  fun test2() {
    val message = messageSource.getMessage("hello.name", arrayOf("kim"), "default", Locale.KOREA)
    assertEquals("hello kim", message)
  }
}