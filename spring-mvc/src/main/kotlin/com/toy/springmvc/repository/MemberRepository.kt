package com.toy.springmvc.repository

import com.toy.springmvc.domain.Member
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository

interface MemberRepository: CrudRepository<Member, Long> {

  fun findAll(pageable: Pageable): Page<Member>
}