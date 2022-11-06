package com.lecture.fsmysql.domain.member.service

import com.lecture.fsmysql.domain.member.dto.MemberNicknameHistoryDto
import com.lecture.fsmysql.domain.member.entity.Member
import com.lecture.fsmysql.domain.member.repository.MemberNicknameHistoryRepository
import com.lecture.fsmysql.domain.member.repository.MemberRepository
import org.springframework.stereotype.Service

@Service
class MemberReadService(
  private val memberRepository: MemberRepository,
  private val memberNicknameHistoryRepository: MemberNicknameHistoryRepository
) {

  fun getMember(id: Long): Member {
    return memberRepository.findById(id) ?: throw RuntimeException("not found...")
  }

  fun getNicknameHistory(id: Long): List<MemberNicknameHistoryDto> {
    return memberNicknameHistoryRepository.findAllByMemberId(id)
      .map { MemberNicknameHistoryDto.of(it) }
  }
}