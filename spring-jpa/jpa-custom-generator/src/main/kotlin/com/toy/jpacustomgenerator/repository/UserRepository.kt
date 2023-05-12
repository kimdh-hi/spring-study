package com.toy.jpacustomgenerator.repository

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.ExpressionUtils.and
import com.querydsl.jpa.impl.JPAQueryFactory
import com.toy.jpacustomgenerator.common.SliceResponseVO
import com.toy.jpacustomgenerator.domain.QUser
import com.toy.jpacustomgenerator.domain.QUser.user
import com.toy.jpacustomgenerator.domain.User
import com.toy.jpacustomgenerator.vo.QUserResponseVO
import com.toy.jpacustomgenerator.vo.UserResponseVO
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, String>, UserRepositoryCustom

interface UserRepositoryCustom {
  fun getList(lastId: String? = null, size: Long): List<UserResponseVO>
}

class UserRepositoryImpl(
  private val query: JPAQueryFactory
): UserRepositoryCustom {

  override fun getList(lastId: String?, size: Long): List<UserResponseVO> {
    return query.select(
      QUserResponseVO(
        user.id, user.name, user.createdDate
      )
    )
      .from(user)
      .where(
        paging(lastId)
      )
      .limit(size)
      .orderBy(user.id.desc())
      .fetch()
  }

  private fun paging(lastId: String?): BooleanBuilder {
    return BooleanBuilder().apply {
      lastId?.let { and(user.id.lt(lastId)) }
    }
  }
}