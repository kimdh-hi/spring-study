package com.toy.springquerydsl.`05-projection`

import com.querydsl.core.types.ExpressionUtils
import com.querydsl.core.types.Projections
import com.querydsl.jpa.JPAExpressions
import com.toy.springquerydsl.`00-base`.TestBase
import com.toy.springquerydsl.domain.QMember
import com.toy.springquerydsl.domain.QMember.member
import com.toy.springquerydsl.vo.UserResponseVO
import org.junit.jupiter.api.Test

class `03-Projections`: TestBase() {

  // Projections... 기본생성자를 필요로 함
  @Test
  fun `Projections bean`() {
    val result = query
      .select(
        Projections.bean(UserResponseVO::class.java,
          member.username, member.age))
      .from(member)
      .fetch()

    println(result)
  }

  @Test
  fun `Projections fields`() {
    val result = query
      .select(
        Projections.fields(UserResponseVO::class.java,
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
        Projections.fields(UserResponseVO::class.java,
          member.username.`as`("name"),
          ExpressionUtils.`as`(
            JPAExpressions
              .select(m2.age.avg())
              .from(m2), "age")
        )
      )
      .from(member)
      .fetch()

    println(result)
  }
}