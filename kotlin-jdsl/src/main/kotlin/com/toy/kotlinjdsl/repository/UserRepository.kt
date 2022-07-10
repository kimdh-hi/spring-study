package com.toy.kotlinjdsl.repository

import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.spring.data.SpringDataQueryFactory
import com.linecorp.kotlinjdsl.spring.data.singleQuery
import com.toy.kotlinjdsl.domain.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

interface UserRepository: CrudRepository<User, String>, UserQuery

interface UserQuery {
  fun get(id: String): User?
}

@Repository
class UserQueryImpl(
  private val queryFactory: SpringDataQueryFactory
): UserQuery {
  override fun get(id: String): User? {
    return queryFactory.singleQuery {
      select(entity(User::class))
      from(entity(User::class))
      where(col(User::id).equal(id))
    }
  }
}