package com.toy.jpafieldencrypt.repository

import com.toy.jpafieldencrypt.domain.Member
import com.toy.jpafieldencrypt.domain.User
import org.springframework.data.repository.CrudRepository

interface MemberRepository: CrudRepository<Member, String> {
  fun findAllByNameContaining(name: String): List<Member>

  fun findByName(name: String): Member?
}