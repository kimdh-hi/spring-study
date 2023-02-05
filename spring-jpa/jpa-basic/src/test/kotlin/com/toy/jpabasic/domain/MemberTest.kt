package com.toy.jpabasic.domain

import com.toy.jpabasic.repository.MemberRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceException
import java.lang.StringBuilder as StringBuilder1

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Transactional
class MemberTest(
  private val memberRepository: MemberRepository,
  private val entityManager: EntityManager
) {

  @Test
  fun columnLength() {
    val nameBuilder = StringBuilder()
    for(i in 0 until 50) {
      nameBuilder.append("a")
    }
    val m1 = Member(name = nameBuilder.toString())

    nameBuilder.clear()
    for(i in 0 until 50) {
      nameBuilder.append("김")
    }
    val m2 = Member(name = nameBuilder.toString())

    memberRepository.save(m1)
    memberRepository.save(m2)

    entityManager.flush()
  }

  @Test
  fun columnLength2() {
    val nameBuilder = StringBuilder()
    for(i in 0 until 51) {
      nameBuilder.append("a")
    }
    val m1 = Member(name = nameBuilder.toString())

    nameBuilder.clear()
    for(i in 0 until 51) {
      nameBuilder.append("김")
    }
    val m2 = Member(name = nameBuilder.toString())

    memberRepository.save(m1)
    memberRepository.save(m2)

    assertThrows(PersistenceException::class.java) {
      entityManager.flush()
    }
  }
}