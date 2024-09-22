package com.toy.springquerydsl.aggregation

import com.toy.springquerydsl.base.BaseTest
import com.toy.springquerydsl.domain.QMember.member
import com.toy.springquerydsl.domain.QTeam.team
import com.toy.springquerydsl.vo.QMemberAggregationVO
import com.toy.springquerydsl.vo.QMemberGroupByVO
import org.junit.jupiter.api.Test

class AggregationTest(
): BaseTest() {

  @Test
  fun aggregation() {
    val result = query.select(
      QMemberAggregationVO(
        member.count(),
        member.age.sum(),
        member.age.avg(),
        member.age.max()
      )
    )
      .from(member)
      .fetch().toList()

    println(result)
  }

  @Test
  fun `groupBy`() {
    val result = query.select(
        QMemberGroupByVO(team.name, member.age.avg())
      )
      .from(member)
      .join(member.team, team)
      .groupBy(member.team, team)
      .fetch()

    println(result)
  }
}
