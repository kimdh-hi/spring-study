package com.toy.jpabasic.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.toy.jpabasic.domain.AuthorityEnumPathSupporter
import com.toy.jpabasic.domain.QRole.role
import com.toy.jpabasic.domain.QUser.user
import com.toy.jpabasic.domain.User
import com.toy.jpabasic.vo.QUserListResponseVO
import com.toy.jpabasic.vo.UserListResponseVO

class UserRepositoryImpl(
  private val query: JPAQueryFactory
): UserRepositoryCustom {

  // JPQL 의 distinct 처럼 동작하지 않음 ..
  // QueryProject 대상으로는 distinct 가 안 먹는건가 ?
  override fun searchList(): MutableList<UserListResponseVO> {
    return query.select(QUserListResponseVO(
      user.id, user.username, AuthorityEnumPathSupporter.authorityCaseBuilder
    )).distinct()
      .from(user)
      .join(user.role, role)
      .join(role.authorities, AuthorityEnumPathSupporter.enumPath)
      .fetch()
  }

  // distinct 예상한 대로 동작 ..
  override fun searchListV2(): MutableList<User> {
    return query.select(user).distinct()
      .from(user)
      .join(user.role, role)
      .join(role.authorities, AuthorityEnumPathSupporter.enumPath)
      .fetch()
  }
}