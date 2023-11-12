package com.toy.springaop.internalcall

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

@SpringBootTest
@Import(CallLogAspect::class)
class CallServiceV3Test @Autowired constructor(
  private val callServiceV3: CallServiceV3
) {

  @Test
  fun external() {
    callServiceV3.external()
  }
}