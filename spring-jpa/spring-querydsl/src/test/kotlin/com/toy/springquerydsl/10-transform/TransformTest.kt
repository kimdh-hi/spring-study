package com.toy.springquerydsl.`10-transform`

import com.querydsl.core.group.GroupBy.groupBy
import com.querydsl.core.group.GroupBy.list
import com.toy.springquerydsl.`00-base`.TestBase
import com.toy.springquerydsl.domain.QMember
import com.toy.springquerydsl.domain.QMember.member
import com.toy.springquerydsl.domain.QTeam
import com.toy.springquerydsl.domain.QTeam.team
import com.toy.springquerydsl.vo.QMemberTransformVO
import org.junit.jupiter.api.Test

class TransformTest: TestBase() {

  @Test
  fun transform() {
    val result = query.select(member)
      .from(member)
      .join(member.team, team)
      .orderBy(member.age.desc())
      .transform(groupBy(team.id, team.name).`as`(list(QMemberTransformVO(member.username, member.age))))

    result.keys.forEach {
      println("$it: ${result[it]}")
    }
  }
}