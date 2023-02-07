package com.toy.jpafulltext.repository

import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import com.toy.jpafulltext.domain.User

class UserRepositoryImpl(
  private val query: JPAQueryFactory
): UserRepositoryCustom {

  override fun searchByDescription(description: String): List<User> {
    TODO("Not yet implemented")
  }
}