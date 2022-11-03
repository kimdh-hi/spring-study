package com.lecture.fsmysql.controler

import com.lecture.fsmysql.domain.member.dto.MemberCreateCommand
import com.lecture.fsmysql.domain.member.entity.Member
import com.lecture.fsmysql.domain.member.service.MemberWriteService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberController(
  private val memberWriteService: MemberWriteService
) {

  @PostMapping("/members")
  fun create(@RequestBody command: MemberCreateCommand): Member {
    return memberWriteService.create(command)
  }
}