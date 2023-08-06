package com.toy.jpaeventlistener.repository

import com.toy.jpaeventlistener.domain.Member
import com.toy.jpaeventlistener.domain.MemberSpaceType
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository: JpaRepository<Member, String> {
  fun findAllByEntrySpaceIdAndEntrySpaceType(entrySpaceId: String, entrySpaceType: MemberSpaceType): List<Member>
}