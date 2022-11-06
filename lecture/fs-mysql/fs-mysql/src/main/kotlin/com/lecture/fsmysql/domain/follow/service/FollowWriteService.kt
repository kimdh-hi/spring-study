package com.lecture.fsmysql.domain.follow.service

import com.lecture.fsmysql.domain.follow.entity.Follow
import com.lecture.fsmysql.domain.follow.repository.FollowRepository
import com.lecture.fsmysql.domain.member.dto.MemberDto
import org.springframework.stereotype.Service

@Service
class FollowWriteService(
  private val followRepository: FollowRepository
) {

  fun create(fromMemberDto: MemberDto, toMemberDto: MemberDto) {
    fromToValidation(fromMemberDto.id, toMemberDto.id)
    val follow = Follow(
      fromMemberId = fromMemberDto.id,
      toMemberId = toMemberDto.id
    )
    followRepository.save(follow)
  }

  private fun fromToValidation(fromMemberId: Long, toMemberId: Long) {
    if(fromMemberId == toMemberId)
      throw RuntimeException("from Follow Member and to Follow Member should be different.")
  }
}