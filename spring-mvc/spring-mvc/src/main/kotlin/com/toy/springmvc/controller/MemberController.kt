package com.toy.springmvc.controller

import com.toy.springmvc.domain.Member
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberController {

  @GetMapping("/member/{id}")
  fun get(@PathVariable("id") member: Member): Member {
    return member
  }
}