package com.toy.springboot3.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.toy.springboot3.domain.QUser.user
import com.toy.springboot3.domain.User
import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<User, String>, UserRepositoryCustom

interface UserRepositoryCustom {
  fun customFindById(id: String): User?
}

class UserRepositoryImpl(
  private val query: JPAQueryFactory
): UserRepositoryCustom {

  override fun customFindById(id: String): User? {
    return query.selectFrom(user)
      .where(user.id.eq(id))
      .fetchOne()
  }
}