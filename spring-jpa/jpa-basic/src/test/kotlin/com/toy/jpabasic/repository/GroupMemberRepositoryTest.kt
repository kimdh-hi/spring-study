package com.toy.jpabasic.repository

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class GroupMemberRepositoryTest(
  private val groupMemberRepository: GroupMemberRepository
) {

  @Test
  fun findByGroupIdAndMemberId() {
    //given
    val groupId = "group-01"
    val memberId = "member-01"

    //when
    val groupMember = groupMemberRepository.findByGroupIdAndMemberId(groupId, memberId)

    //then
    assertNotNull(groupMember)
    println(groupMember)
  }
}