package com.lecture.fsmysql.application.usecase

import com.lecture.fsmysql.domain.follow.service.FollowReadService
import com.lecture.fsmysql.domain.member.dto.MemberDto
import com.lecture.fsmysql.domain.member.service.MemberReadService
import org.springframework.stereotype.Component

@Component
class GetFollowingMembersUsacase(
  private val followReadService: FollowReadService,
  private val memberReadService: MemberReadService
) {

  fun execute(memberId: Long): List<MemberDto> {
    val followings = followReadService.getFollowings(memberId)
    return memberReadService.getMembers(
      followings.map { it.toMemberId }
    )
  }
}