package com.lecture.divelog.test

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(args = ["--app.name=diveLogTest"])
class ApplicationArgumentsTest {

  @Test
  fun applicationArguments(@Autowired arguments: ApplicationArguments) {
    assertThat(arguments.optionNames.contains("app.name"))
    assertThat(arguments.getOptionValues("app.name").contains("diveLogTest"))
  }
}