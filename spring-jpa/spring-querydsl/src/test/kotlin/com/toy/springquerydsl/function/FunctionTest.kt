package com.toy.springquerydsl.function

import com.querydsl.core.types.dsl.Expressions
import com.toy.springquerydsl.base.BaseTest
import com.toy.springquerydsl.domain.QMember.member
import org.junit.jupiter.api.Test

/*
org.hibernate.dialect.H2Dialect
org.hibernate.dialect.MySQL8Dialect

각 Dialect 에 등록된 function 만 사용가능
 */
class FunctionTest: BaseTest() {

  @Test
  fun replace() {
    val result = query.select(
      Expressions.stringTemplate(
        "function('replace', {0}, {1}, {2})", member.username,  "member", "M"))
      .from(member)
      .fetch()

    println(result)
  }
}
