package com.toy.jpabasic.repository

import com.toy.jpabasic.domain.Member
import org.springframework.data.repository.CrudRepository

interface MemberRepository: CrudRepository<Member, String> {
}