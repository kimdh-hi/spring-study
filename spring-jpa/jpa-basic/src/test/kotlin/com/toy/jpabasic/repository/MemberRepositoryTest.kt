package com.toy.jpabasic.repository

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class MemberRepositoryTest(
  private val memberRepository: MemberRepository
) {

  @Test
  fun get() {
    val member = memberRepository.findByIdOrNull("member-01")!!
    println(member)
  }
}