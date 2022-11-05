package com.lecture.fsmysql.domain.member.service

import com.lecture.fsmysql.domain.member.dto.MemberCreateCommand
import com.lecture.fsmysql.domain.member.entity.Member
import com.lecture.fsmysql.domain.member.repository.MemberRepository
import org.springframework.stereotype.Service

@Service
class MemberWriteService(
  private val memberRepository: MemberRepository
) {

  fun create(command: MemberCreateCommand): Member {
    val member = Member(
      nickname = command.nickname,
      email = command.email,
      birthday = command.birthday
    )
    return memberRepository.save(member)
  }

  fun updateNickname(id: Long, changeNickname: String): Member {
    val member = memberRepository.findById(id) ?: throw RuntimeException("not found...")
    member.changeNickname(changeNickname)
    return memberRepository.save(member)
  }
}