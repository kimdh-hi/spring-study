package com.toy.springthymeleaf.mail

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class MailSenderTest @Autowired constructor(
  private val mailSender: MailSender
) {

  @Test
  fun test() {
    val context = mapOf(
      "a" to "aa",
      "b" to "bb"
    )

    mailSender.send("to@gmail.com", "test", context, "ko")
  }
}
