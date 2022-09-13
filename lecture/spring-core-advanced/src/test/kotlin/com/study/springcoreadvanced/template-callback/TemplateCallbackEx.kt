package com.study.springcoreadvanced.`template-callback`

import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

class TemplateCallbackEx {

  private val log = LoggerFactory.getLogger(javaClass)

  @Test
  fun `ex`() {
    val timeLogTemplate = TimeLogTemplate()
    timeLogTemplate.execute {
      log.info("logic ...")
    }
  }
}