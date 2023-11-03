package com.toy.springaop.service

import com.toy.springaop.annotation.ClassAop
import com.toy.springaop.annotation.MethodAop
import org.springframework.stereotype.Component

@ClassAop
@Component
class MemberServiceImpl: MemberService {

  override fun test(param: String): String = "test"

  @MethodAop("value")
  fun internal() = "test"
}