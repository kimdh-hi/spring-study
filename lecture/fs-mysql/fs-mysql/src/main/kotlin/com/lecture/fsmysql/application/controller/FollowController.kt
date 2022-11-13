package com.lecture.fsmysql.application.controller

import com.lecture.fsmysql.application.usecase.CreateFollowMemberUsecase
import com.lecture.fsmysql.application.usecase.GetFollowingMembersUsacase
import com.lecture.fsmysql.domain.member.dto.MemberDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/follow")
class FollowController(
  private val createFollowMemberUsecase: CreateFollowMemberUsecase,
  private val getFollowingMembersUsacase: GetFollowingMembersUsacase
) {

  @PostMapping("/{fromId}/{toId}")
  fun create(
    @PathVariable fromId: Long,
    @PathVariable toId: Long
  ) {
    createFollowMemberUsecase.execute(fromId, toId)
  }

  @GetMapping("/{memberId}")
  fun getFollowingMembers(@PathVariable memberId: Long): List<MemberDto> {
    return getFollowingMembersUsacase.execute(memberId)
  }
}