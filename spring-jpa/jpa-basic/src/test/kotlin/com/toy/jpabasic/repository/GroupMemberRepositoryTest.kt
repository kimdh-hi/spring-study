package com.toy.jpabasic.repository

import com.toy.jpabasic.base.TestData
import com.toy.jpabasic.domain.GroupMember
import org.hibernate.exception.ConstraintViolationException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.TestConstructor
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Transactional
class GroupMemberRepositoryTest(
  private val groupMemberRepository: GroupMemberRepository,
  private val groupMemberOptionRepository: GroupMemberOptionRepository,
  private val memberRepository: MemberRepository,
  private val groupRepository: GroupRepository,
  private val em: EntityManager
) {

  @Test
  fun findById() {
    //given
    val groupId = "group-01"
    val memberId = "member-01"
    val groupMemberId = GroupMember.ID(groupId, memberId)

    //when
    val groupMember = groupMemberRepository.findByIdOrNull(groupMemberId)

    //then
    println(groupMember)
  }

  @Test
  fun save() {
    val sbAppId = "1111"
    val sbApiToken = "1111"
    val member = memberRepository.findByIdOrNull(TestData.MEMBER_01.id!!)!!
    val group = groupRepository.findByIdOrNull(TestData.GROUP_01.id!!)!!

    println(member)
    println(group)

    val groupMember = GroupMember.of(member, group, sbAppId, sbApiToken)

    val savedGroupMember = groupMemberRepository.save(groupMember)

    println(savedGroupMember)
  }

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
    println(groupMember?.groupMemberOption)
  }

  @Test
  fun `findByGroupIdAndMemberId - groupMemberOptionNull`() {
    //given
    val groupId = "group-03"
    val memberId = "member-01"

    //when
    val groupMember = groupMemberRepository.findByGroupIdAndMemberId(groupId, memberId)

    //then
    assertNotNull(groupMember)
    println(groupMember)
    println(groupMember?.groupMemberOption)
    em.flush()
    em.clear()

    val groupMemberOption = groupMemberOptionRepository.findByIdOrNull("gmo-1")!!
    val targetGroupMember = groupMemberRepository.findByGroupIdAndMemberId(groupId, memberId)
    targetGroupMember?.groupMemberOption = groupMemberOption
    em.flush()
    em.clear()

    val optionUpdatedGroupMember = groupMemberRepository.findByGroupIdAndMemberId(groupId, memberId)
    println(optionUpdatedGroupMember?.groupMemberOption)
  }

  @Test
  fun findByMemberId() {
    //given
    val memberId = "member-01"

    //when
    val groupMembers = groupMemberRepository.findByMemberId(memberId)

    //then
    assertAll({
      assertTrue(groupMembers.isNotEmpty())
      groupMembers.forEach {
        assertTrue(it.member.id == memberId)
      }
    })

    println(groupMembers)
  }
}