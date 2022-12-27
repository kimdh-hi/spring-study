package com.toy.springquerydsl.`03-case`

import com.querydsl.core.types.dsl.CaseBuilder
import com.toy.springquerydsl.`00-base`.BaseTest
import com.toy.springquerydsl.domain.QMember.member
import org.junit.jupiter.api.Test

/**
 * select, where, orderBy 에서 사용 가능
 */
class Case: BaseTest() {

  @Test
  fun `select case1`() {
    val result = query.select(
      member.age
        .`when`(20).then("스무살")
        .`when`(30).then("서른살")
        .otherwise("몇 살?")
    )
      .from(member)
      .fetch()

    println(result)
  }

  @Test
  fun `select case2`() {
    val result = query.select(
      CaseBuilder()
        .`when`(member.age.between(20, 30)).then("20대")
        .`when`(member.age.between(30, 40)).then("30대")
        .otherwise("몰라 ㅎㅎ")
    )
      .from(member)
      .fetch()

    println(result)
  }

  /**
   * 0~10살, 10~20살, 20~30살 30~xx 정렬
   */
  @Test
  fun `orderBy case1`() {
    val rankPath = CaseBuilder()
      .`when`(member.age.between(0, 10)).then(0)
      .`when`(member.age.between(10, 20)).then(1)
      .`when`(member.age.between(20, 30)).then(2)
      .`when`(member.age.gt(30)).then(3)
      .otherwise(4)

    val members = query
      .select(member.username, member.age, rankPath)
      .from(member)
      .orderBy(rankPath.desc())
      .fetch()

    println(members)
  }
}