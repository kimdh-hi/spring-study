package com.lecture.fsmysql.domain.follow.service

import com.lecture.fsmysql.domain.follow.entity.Follow
import com.lecture.fsmysql.domain.follow.repository.FollowRepository
import org.springframework.stereotype.Repository

@Repository
class FollowReadService(
  private val followRepository: FollowRepository
) {

  fun getFollowings(memberId: Long): List<Follow> {
    return followRepository.findAllByFromMemberId(memberId)
  }

  fun getFollowers(memberId: Long): List<Follow> {
    return followRepository.findAllByToMemberId(memberId)
  }
}