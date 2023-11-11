package com.toy.springaop.internalcall

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

@SpringBootTest
@Import(CallLogAspect::class)
class CallServiceV2Test @Autowired constructor(
  private val callServiceV2: CallServiceV2
) {

  @Test
  fun external() {
    callServiceV2.external()
  }
}