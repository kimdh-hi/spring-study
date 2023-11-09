package com.toy.springaop.internalcall

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

@SpringBootTest
@Import(CallLogAspect::class)
class CallServiceV0Test @Autowired constructor(
  private val callServiceV0: CallServiceV0
) {

  // external 메서드는 aop 적용
  // external 메서드 내부에서 호출된 internal 메서드는 aop 미적용
  @Test
  fun external() {
    callServiceV0.external()
  }

  @Test
  fun internal() {
    callServiceV0.internal()
  }
}