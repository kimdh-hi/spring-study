package com.toy.jpabasic.repository

import com.toy.jpabasic.domain.Member
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
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

  @Test
  fun findAllByIdsNotFoundTest() {
    //given
    val m1 = Member(name = "test1", age = 10)
    val m2 = Member(name = "test2", age = 20)
    val m3 = Member(name = "test3", age = 30)
    val sm1 = memberRepository.save(m1)
    val sm2 = memberRepository.save(m2)
    val sm3 = memberRepository.save(m3)
    entityManager.flush()
    entityManager.clear()
    val ids = listOf(sm1.id, sm2.id, sm3.id, "not-exists-id")

    //when
    assertDoesNotThrow { memberRepository.findAllById(ids) }
    // 존재하지 않는 경우 pass (예외 xx)
  }

  @Test
  fun `ColumnDefault null test`() {
    //given
    val member = Member(name = "name")

    //when
    val savedMember = memberRepository.save(member)
    entityManager.flush()
    entityManager.clear()

    //then
    assertNull(savedMember.test) // null 삽입시 @ColumnDefault 동작 x
  }

  @Test
  fun `ColumnDefault dynamicInsert`() {
    //given
    val member = Member(name = "name")

    //when
    val savedMember = memberRepository.save(member)
    entityManager.flush()
    entityManager.clear()

    //then
    assertEquals(true, savedMember.test)
  }

}