package com.toy.springquerydsl

import com.querydsl.jpa.impl.JPAQueryFactory
import com.toy.springquerydsl.domain.Member
import com.toy.springquerydsl.domain.QMember
import com.toy.springquerydsl.domain.Team
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@SpringBootTest
@Transactional
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class QuerydslBasicTest(private val em: EntityManager) {

  @BeforeEach
  fun beforeEach() {
    val teamA = Team(name = "teamA")
    val teamB = Team(name = "teamB")
    em.persist(teamA)
    em.persist(teamB)

    val memeber1 = Member(username = "member1", age = 20, team = teamA)
    val memeber2 = Member(username = "member2", age = 21, team = teamA)
    val memeber3 = Member(username = "member3", age = 30, team = teamB)
    val memeber4 = Member(username = "member4", age = 31, team = teamB)
    em.persist(memeber1)
    em.persist(memeber2)
    em.persist(memeber3)
    em.persist(memeber4)
  }

  @Test
  fun `JPQL - find member1`() {
    val member = em.createQuery("select m from Member m where m.username = :username", Member::class.java)
      .setParameter("username", "member1")
      .singleResult

    Assertions.assertThat(member.username).isEqualTo("member1")
  }

  @Test
  fun `Querydsl - find member1`() {
    val query = JPAQueryFactory(em)
    val m = QMember("m") // alias
    val member = query.select(m)
      .from(m)
      .where(m.username.eq("member1"))
      .fetchOne()

    Assertions.assertThat(member!!.username).isEqualTo("member1")
  }
}