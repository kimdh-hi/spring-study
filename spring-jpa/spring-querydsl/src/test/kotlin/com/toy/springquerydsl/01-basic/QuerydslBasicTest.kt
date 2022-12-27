package com.toy.springquerydsl.`01-basic`

import com.querydsl.jpa.impl.JPAQueryFactory
import com.toy.springquerydsl.`00-base`.BaseTest
import com.toy.springquerydsl.domain.Member
import com.toy.springquerydsl.domain.QMember
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class QuerydslBasicTest: BaseTest() {

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