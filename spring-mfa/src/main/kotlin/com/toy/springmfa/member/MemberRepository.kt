package com.toy.springmfa.member

import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long> {

  fun findByUsername(username: String): Member?
}
