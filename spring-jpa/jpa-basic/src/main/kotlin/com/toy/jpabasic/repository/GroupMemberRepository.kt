package com.toy.jpabasic.repository

import com.toy.jpabasic.domain.GroupMember
import org.springframework.data.repository.CrudRepository

interface GroupMemberRepository: CrudRepository<GroupMember, String> {

  fun findByGroupIdAndMemberId(groupId: String, memberId: String): GroupMember?
}