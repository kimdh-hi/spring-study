package com.lecture.fsmysql.application.usecase

import com.lecture.fsmysql.domain.follow.service.FollowWriteService
import com.lecture.fsmysql.domain.member.service.MemberReadService
import org.springframework.stereotype.Component

/**
 * 각 도메인 서비스 레이어에서 비즈니스 로직을 처리
 * usecase 는 단지 두 도메인을 조합해주는 역할을 수행
 * 최대한 비즈니스 로직이 포함되지 않도록 한다.
 */
@Component
class CreateFollowMemberUsecase(
  private val memberReadService: MemberReadService,
  private val followWriteService: FollowWriteService
) {

  fun execute(fromMemberId: Long, toMemberId: Long) {
    val fromMember = memberReadService.getMember(fromMemberId)
    val toMember = memberReadService.getMember(toMemberId)
    followWriteService.create(fromMember, toMember)
  }
}