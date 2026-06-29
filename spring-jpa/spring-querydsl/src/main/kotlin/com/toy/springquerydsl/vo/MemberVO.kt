package com.toy.springquerydsl.vo

import com.querydsl.core.annotations.QueryProjection
import com.toy.springquerydsl.common.NoArg
import com.toy.springquerydsl.domain.Member

@NoArg
data class MemberResponseVO @QueryProjection constructor(
  val username: String,
  val age: Int,
)

data class MemberDetailsResponseVO @QueryProjection constructor(
  val userId: Long,
  val username: String,
  val age: Int,
  val teamId: Long,
  val teamName: String
)

data class MemberSearchVO(
  val username: String? = null,
  val age: Int? = null,
)

data class MemberAggregationVO @QueryProjection constructor(
  val count: Long,
  val ageSum: Int,
  val ageAvg: Double,
  val ageMax: Int
)

data class MemberGroupByVO @QueryProjection constructor(
  val teamName: String,
  val age: Double
)

data class MemberTransformVO @QueryProjection constructor(
  val username: String,
  val age: Int,
)

data class MemberProjectionTestVO(
  val username: String,
  val innerMemberUsername: String?,
) {
  @QueryProjection constructor(member: Member): this(
    username = member.username,
    innerMemberUsername = member.findInnerMemberUsername(),
  )
}
