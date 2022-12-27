package com.toy.springquerydsl.`05-projection`

import com.toy.springquerydsl.`00-base`.BaseTest
import com.toy.springquerydsl.domain.QMember.member
import com.toy.springquerydsl.vo.QMemberResponseVO
import org.junit.jupiter.api.Test

class `02-VO-Projection`: BaseTest() {

  /**
   * Projections bean, field 등의 다른 방법이 있지만 가장 권장하는 방식
   */
  @Test
  fun `vo - projection`() {
    val result = query
      .select(QMemberResponseVO(member.username, member.age))
      .from(member)
      .fetch()

    println(result)
  }
}