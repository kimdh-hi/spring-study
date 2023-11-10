package com.toy.springaop.internalcall

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

@SpringBootTest
@Import(CallLogAspect::class)
class CallServiceV1Test @Autowired constructor(
  private val callServiceV1: CallServiceV1
) {

  // callServiceV1 에서 자기자신을 주입받아서 사용. 이 때 주입되는 callServiceV1 은 프록시 (this 로 호출하는 것과 다른 것.)
  // 내부 메서드 호출시 프록시로 주입받아진 callServiceV1 의 메서드가 호출되는 것.
  @Test
  fun external() {
    callServiceV1.external()
  }
}