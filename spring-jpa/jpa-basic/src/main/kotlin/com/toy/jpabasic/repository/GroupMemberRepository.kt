package com.toy.jpabasic.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.toy.jpabasic.domain.GroupMember
import com.toy.jpabasic.domain.GroupMemberOption
import org.springframework.data.repository.CrudRepository

interface GroupMemberRepository: CrudRepository<GroupMember, GroupMember.ID> {

  fun findByGroupIdAndMemberId(groupId: String, memberId: String): GroupMember?
  fun findByMemberId(memberId: String): List<GroupMember>
  fun findByGroupId(groupId: String): List<GroupMember>
  fun existsByMemberId(memberId: String): Boolean
  fun existsByGroupId(groupId: String): Boolean
}

interface GroupMemberOptionRepository: CrudRepository<GroupMemberOption, String> {

}