package com.toy.jpaeventlistener.repository

import com.toy.jpaeventlistener.domain.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository: JpaRepository<Member, String> {

}