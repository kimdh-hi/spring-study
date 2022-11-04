package com.lecture.fsmysql.domain.member.service

import com.lecture.fsmysql.domain.member.entity.Member
import com.lecture.fsmysql.domain.member.repository.MemberRepository
import org.springframework.stereotype.Service

@Service
class MemberReadService(
  private val memberRepository: MemberRepository
) {

  fun getMember(id: Long): Member {
    return memberRepository.findById(id) ?: throw RuntimeException("not found...")
  }
}