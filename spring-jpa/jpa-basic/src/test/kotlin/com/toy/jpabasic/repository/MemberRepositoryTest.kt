package com.toy.jpabasic.repository

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.TestConstructor
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Transactional
class MemberRepositoryTest(
  private val memberRepository: MemberRepository,
  private val groupMemberRepository: GroupMemberRepository
) {

  @Test
  fun get() {
    val member = memberRepository.findByIdOrNull("member-01")!!
    println(member)
  }

  @Test
  fun delete() {
    memberRepository.deleteById("member-01")
    assertTrue(!groupMemberRepository.existsByMemberId("member-01"))
  }
}