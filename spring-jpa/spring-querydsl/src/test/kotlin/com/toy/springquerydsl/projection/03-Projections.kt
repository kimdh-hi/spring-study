package com.toy.springquerydsl.projection

import com.querydsl.core.types.ExpressionUtils
import com.querydsl.core.types.Projections
import com.querydsl.jpa.JPAExpressions
import com.toy.springquerydsl.base.BaseTest
import com.toy.springquerydsl.domain.QMember
import com.toy.springquerydsl.domain.QMember.member
import com.toy.springquerydsl.vo.MemberResponseVO
import org.junit.jupiter.api.Test

class `03-Projections`: BaseTest() {

  // Projections... 기본생성자를 필요로 함
  @Test
  fun `Projections bean`() {
    val result = query
      .select(
        Projections.bean(MemberResponseVO::class.java,
          member.username, member.age))
      .from(member)
      .fetch()

    println(result)
  }

  @Test
  fun `Projections fields`() {
    val result = query
      .select(
        Projections.fields(MemberResponseVO::class.java,
          member.username, member.age)
      )
      .from(member)
      .fetch()

    println(result)
  }

  @Test
  fun `Projections fields2`() {
    val m2 = QMember("m2")

    val result = query
      .select(
        Projections.fields(MemberResponseVO::class.java,
          member.username.`as`("name"),
          ExpressionUtils.`as`(
            JPAExpressions
              .select(m2.age.max())
              .from(m2), "age")
        )
      )
      .from(member)
      .fetch()

    println(result)
  }
}
