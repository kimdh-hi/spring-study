package com.toy.springquerydsl.`04-concat`

import com.toy.springquerydsl.`00-base`.TestBase
import com.toy.springquerydsl.domain.QMember.member
import org.junit.jupiter.api.Test

class Concat: TestBase() {

  @Test
  fun concat() {
    val members = query.select(
      member.username.concat("_").concat(member.age.stringValue())
    )
      .from(member)
      .fetch()

    println(members)
  }
}