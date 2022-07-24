package com.toy.springquerydsl.`05-projection`

import com.querydsl.core.annotations.QueryProjection
import com.toy.springquerydsl.`00-base`.TestBase
import com.toy.springquerydsl.domain.QMember.member
import org.junit.jupiter.api.Test

class `01-Tuple-Projection`: TestBase() {

  @Test
  fun `조회 대상이 둘 이상일 때 - Tuple 타입으로 조회`() {
    val result = query
      .select(member.username, member.age)
      .from(member)
      .fetch()

    result.forEach {
      println("username: ${it.get(member.username)}, age: ${it.get(member.age)}")
    }
  }
}