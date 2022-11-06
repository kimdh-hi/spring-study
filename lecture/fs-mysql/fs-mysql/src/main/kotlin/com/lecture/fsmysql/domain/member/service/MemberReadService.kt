package com.lecture.fsmysql.domain.member.service

import com.lecture.fsmysql.domain.member.dto.MemberDto
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

  fun getMember(id: Long): MemberDto {
    val member = memberRepository.findById(id) ?: throw RuntimeException("not found...")
    return MemberDto.of(member)
  }

  fun getNicknameHistory(id: Long): List<MemberNicknameHistoryDto> {
    return memberNicknameHistoryRepository.findAllByMemberId(id)
      .map { MemberNicknameHistoryDto.of(it) }
  }

  fun getMembers(ids: List<Long>): List<MemberDto> {
    return memberRepository.findByAllIdIn(ids)
      .map { MemberDto.of(it) }
  }
}