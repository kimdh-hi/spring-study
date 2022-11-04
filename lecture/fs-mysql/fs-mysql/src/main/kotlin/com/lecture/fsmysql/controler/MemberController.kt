package com.lecture.fsmysql.controler

import com.lecture.fsmysql.domain.member.dto.MemberCreateCommand
import com.lecture.fsmysql.domain.member.dto.MemberDto
import com.lecture.fsmysql.domain.member.entity.Member
import com.lecture.fsmysql.domain.member.service.MemberReadService
import com.lecture.fsmysql.domain.member.service.MemberWriteService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
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
    val member = memberReadService.getMember(id)
    return MemberDto.of(member)
  }
}