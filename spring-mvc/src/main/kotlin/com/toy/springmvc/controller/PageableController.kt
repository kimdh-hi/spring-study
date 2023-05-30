package com.toy.springmvc.controller

import com.toy.springmvc.domain.Member
import com.toy.springmvc.repository.MemberRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/pageable")
class PageableController(
  private val memberRepository: MemberRepository
) {

  @GetMapping
  fun search(pageable: Pageable): ResponseEntity<Page<Member>> {
    return ResponseEntity.ok(memberRepository.findAll(pageable))
  }
}