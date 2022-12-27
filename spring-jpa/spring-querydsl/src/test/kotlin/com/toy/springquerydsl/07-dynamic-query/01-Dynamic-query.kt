package com.toy.springquerydsl.`07-dynamic-query`

import com.querydsl.core.BooleanBuilder
import com.toy.springquerydsl.`00-base`.BaseTest
import com.toy.springquerydsl.domain.QMember.member
import com.toy.springquerydsl.domain.QTeam.*
import com.toy.springquerydsl.vo.QMemberDetailsResponseVO
import org.junit.jupiter.api.Test

class `01-Dynamic-query`: BaseTest() {

  @Test
  fun `DynamicSearch`() {
    val searchVO = MemberSearchVO(username = "member1", age = 20)

    val result = query
      .select(
        QMemberDetailsResponseVO(
          member.id, member.username, member.age,
          team.id, team.name))
      .from(member)
      .leftJoin(member.team, team)
      .where(search(searchVO))
      .fetch()

    println(result)
  }

  private fun search(searchVO: MemberSearchVO): BooleanBuilder {
    val builder = BooleanBuilder()
    searchVO.username?.let {
      builder.and(member.username.containsIgnoreCase(it))
    }

    searchVO.age?.let {
      builder.and(member.age.eq(it))
    }

    return builder
  }
}

data class MemberSearchVO(
  val username: String? = null,
  val age: Int? = null
)