package com.toy.jpabasic.repository

import com.toy.jpabasic.domain.Member
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.TestConstructor
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Transactional
class MemberRepositoryTest(
  private val memberRepository: MemberRepository,
  private val groupMemberRepository: GroupMemberRepository,
  private val entityManager: EntityManager
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

  @Test
  fun bulkUpdate() {
    //given
    val m1 = Member(name = "test1", age = 10)
    val m2 = Member(name = "test2", age = 20)
    val m3 = Member(name = "test3", age = 30)
    memberRepository.saveAll(listOf(m1, m2, m3))

    //when
    val result = memberRepository.bulkUpdateAddAge(1)

    val member1 = memberRepository.findByName("test3")!!
    assertEquals(member1.age, 30) // 영속성 컨텍스트 조회
    entityManager.flush()
    entityManager.clear()
    val member2 = memberRepository.findByName("test3")!! // db 조회 -> 영속성 컨텍스트 -> 조회
    assertEquals(member2.age, 31)

    //then
    assertEquals(result, 2)
  }

  @Test
  fun bulkUpdateAutoClear() {
    //given
    val m1 = Member(name = "test1", age = 10)
    val m2 = Member(name = "test2", age = 20)
    val m3 = Member(name = "test3", age = 30)
    memberRepository.saveAll(listOf(m1, m2, m3))

    //when
    val result = memberRepository.bulkUpdateAddAgeAutoClear(1)

    val member1 = memberRepository.findByName("test3")!!
    assertEquals(member1.age, 31)

    //then
    assertEquals(result, 2)
  }
}