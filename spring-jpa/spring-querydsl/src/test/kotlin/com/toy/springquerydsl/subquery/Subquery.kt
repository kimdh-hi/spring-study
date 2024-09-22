package com.toy.springquerydsl.subquery

import com.querydsl.jpa.JPAExpressions
import com.toy.springquerydsl.base.BaseTest
import com.toy.springquerydsl.domain.QMember
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class Subquery: BaseTest() {

  @Test
  fun `subquery1 - 나이가 가장 많은 member 조회`() {
    val m1 = QMember("m1")
    val m2 = QMember("m2")

    val members = query
      .selectFrom(m1)
      .where(m1.age.eq(
        JPAExpressions // subquery
          .select(m2.age.max())
          .from(m2)
      ))
      .fetch()

    members.forEach {
      assertEquals(31, it.age)
    }
  }

  @Test
  fun `subquery2 - 나이가 평균보다 같거나 많은 member 조회`() {
    val m1 = QMember("m1")
    val m2 = QMember("m2")

    val members = query
      .selectFrom(m1)
      .where(m1.age.goe(
        JPAExpressions
          .select(m2.age.avg())
          .from(m2)
      ))
      .fetch()

    println(members)
  }

  @Test
  fun `subquery3 - 나이가 10살보다 많은 member 조회`() {
    val m1 = QMember("m1")
    val m2 = QMember("m2")

    val members = query
      .selectFrom(m1)
      .where(m1.age.`in`(
        JPAExpressions
          .select(m2.age)
          .from(m2)
          .where(m2.age.gt(10))
      ))
      .fetch()

    members.forEach {
      assertTrue(it.age > 10)
    }
  }
}
