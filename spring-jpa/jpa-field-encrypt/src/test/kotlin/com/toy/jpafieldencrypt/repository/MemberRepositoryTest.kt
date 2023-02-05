package com.toy.jpafieldencrypt.repository

import com.toy.jpafieldencrypt.domain.Member
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.TestConstructor
import javax.persistence.EntityManager

@DataJpaTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class MemberRepositoryTest(
  private val memberRepository: MemberRepository,
  private val entityManager: EntityManager
) {

  @Test
  fun save() {
    //given
    val member = Member(name = "name")

    //when
    memberRepository.save(member)
    entityManager.flush()
  }
}