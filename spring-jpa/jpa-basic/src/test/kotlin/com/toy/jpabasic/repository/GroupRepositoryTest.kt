package com.toy.jpabasic.repository

import com.toy.jpabasic.domain.Group
import com.toy.jpabasic.domain.GroupMember
import com.toy.jpabasic.domain.Member
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.TestConstructor
import javax.persistence.EntityManager

@DataJpaTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
internal class GroupRepositoryTest(
  private val groupRepository: GroupRepository,
  private val memberRepository: MemberRepository,
  private val groupMemberRepository: GroupMemberRepository,
  private val entityManager: EntityManager
) {

  @Test
  fun `groupMemberUpdate`() {
    //given
    val group1 = Group(name = "group1")
    val group2 = Group(name = "group2")
    val groupEntity1 = groupRepository.save(group1)
    val groupEntity2 = groupRepository.save(group2)

    val member1 = Member(name = "member1")
    val member2 = Member(name = "member2")
    val memberEntity1 = memberRepository.save(member1)
    val memberEntity2 = memberRepository.save(member2)

    val groupMember1 = GroupMember.of(member = memberEntity1, group = groupEntity1)
    val groupMember2 = GroupMember.of(member = memberEntity2, group = groupEntity2)
    val gm1 = groupMemberRepository.save(groupMember1)
    val gm2 = groupMemberRepository.save(groupMember2)

    entityManager.flush()
    entityManager.clear()

    //when
    groupMemberRepository.findByGroupIdAndMemberId(groupEntity1.id!!, memberEntity1.id!!)!!.group = group2

    entityManager.flush()
    entityManager.clear()

    //then
    val result = groupMemberRepository.findByGroupIdAndMemberId(groupEntity1.id!!, memberEntity1.id!!)
    println(result)
  }
}