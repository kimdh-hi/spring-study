package com.toy.jpafulltext.repository

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import com.toy.jpafulltext.domain.QUser
import com.toy.jpafulltext.domain.QUser.user
import com.toy.jpafulltext.domain.User

class UserRepositoryImpl(
  private val query: JPAQueryFactory
): UserRepositoryCustom {

  override fun searchByDescriptionV1(description: String): List<User> {
    return query.selectFrom(user)
      .where(user.description2.contains(description))
      .fetch()
  }

  override fun searchByDescriptionV2(description: String): List<User> {
    val user = QUser("user")
    val fullTextSearchCondition =
      Expressions.numberTemplate(Double::class.javaObjectType, "function('match', {0}, {1})", user.description1, "+$description*")

//    val fullTextSearchCondition =
//      Expressions.numberTemplate(Double::class.javaObjectType, "function('match', {0}, {1})", user.description1, description)

    val booleanBuilder = BooleanBuilder().and(fullTextSearchCondition.gt(0))

    return query.selectFrom(user)
      .where(booleanBuilder)
      .fetch()
  }
}