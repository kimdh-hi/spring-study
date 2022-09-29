package com.toy.jpabasic.repository

import com.toy.jpabasic.base.TestData
import com.toy.jpabasic.domain.GroupMember
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.TestConstructor

@DataJpaTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
internal class GroupRepositoryTest(
  private val groupRepository: GroupRepository,
  private val memberRepository: MemberRepository,
  private val groupMemberRepository: GroupMemberRepository
)