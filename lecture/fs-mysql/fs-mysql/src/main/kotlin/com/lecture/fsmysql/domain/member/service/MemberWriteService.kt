package com.lecture.fsmysql.domain.member.service

import com.lecture.fsmysql.domain.member.dto.MemberCreateCommand
import com.lecture.fsmysql.domain.member.entity.Member
import com.lecture.fsmysql.domain.member.entity.MemberNicknameHistory
import com.lecture.fsmysql.domain.member.repository.MemberNicknameHistoryRepository
import com.lecture.fsmysql.domain.member.repository.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberWriteService(
  private val memberRepository: MemberRepository,
  private val memberNicknameHistoryRepository: MemberNicknameHistoryRepository
) {

  @Transactional
  fun create(command: MemberCreateCommand): Member {
    val member = Member(
      nickname = command.nickname,
      email = command.email,
      birthday = command.birthday
    )
    val savedMember = memberRepository.save(member)
//    val ex = 0/0 // exception. rollback
    saveNicknameHistory(savedMember)
    return savedMember
  }

  fun changeNickname(id: Long, changeNickname: String): Member {
    val member = memberRepository.findById(id) ?: throw RuntimeException("not found...")
    member.changeNickname(changeNickname)
    val changedMember = memberRepository.save(member)
    saveNicknameHistory(changedMember)
    return changedMember
  }

  private fun saveNicknameHistory(member: Member): MemberNicknameHistory {
    val nicknameHistory = MemberNicknameHistory.of(member)
    return memberNicknameHistoryRepository.save(nicknameHistory)
  }
}