package com.toy.springmvc.repository

import com.toy.springmvc.domain.Member
import org.springframework.data.repository.CrudRepository

interface MemberRepository: CrudRepository<Member, Long> {
}