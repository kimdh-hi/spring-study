package com.lecture.fsmysql.application.controller

import com.lecture.fsmysql.domain.member.dto.MemberCreateCommand
import com.lecture.fsmysql.domain.member.dto.MemberDto
import com.lecture.fsmysql.domain.member.dto.MemberNicknameHistoryDto
import com.lecture.fsmysql.domain.member.dto.MemberNicknameUpdateRequestDto
import com.lecture.fsmysql.domain.member.service.MemberReadService
import com.lecture.fsmysql.domain.member.service.MemberWriteService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberController(
  private val memberWriteService: MemberWriteService,
  private val memberReadService: MemberReadService
) {

  @PostMapping("/members")
  fun create(@RequestBody command: MemberCreateCommand): MemberDto {
    val member = memberWriteService.create(command)
    return MemberDto.of(member)
  }

  @GetMapping("/members/{id}")
  fun read(@PathVariable id: Long): MemberDto {
    return memberReadService.getMember(id)
  }

  @PostMapping("/members/{id}/nickname")
  fun changeNickname (
    @PathVariable id: Long,
    @RequestBody requestDto: MemberNicknameUpdateRequestDto
  ): MemberDto {
    val member = memberWriteService.changeNickname(id, requestDto.nickname)
    return MemberDto.of(member)
  }

  @GetMapping("/members/{id}/histories")
  fun nicknameHistories(
    @PathVariable id: Long,
  ): List<MemberNicknameHistoryDto> {
    return memberReadService.getNicknameHistory(id)
  }
}