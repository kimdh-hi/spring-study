package com.toy.kotlinjdsl.repository

import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.querydsl.expression.column
import com.linecorp.kotlinjdsl.spring.data.SpringDataQueryFactory
import com.linecorp.kotlinjdsl.spring.data.singleQuery
import com.toy.kotlinjdsl.domain.User
import com.toy.kotlinjdsl.vo.UserResponseVO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

interface UserRepository: CrudRepository<User, String>, UserQuery

interface UserQuery {
  fun searchV1(pageable: Pageable): Page<User>
  fun searchV2(pageable: Pageable): Page<UserResponseVO>
  fun get(id: String): User?
}

@Repository
class UserQueryImpl(
  private val queryFactory: SpringDataQueryFactory
): UserQuery {

  override fun searchV1(pageable: Pageable): Page<User> {
    return queryFactory.pageQuery(User::class.java, pageable) {
      select(entity(User::class))
      from(entity(User::class))
    }
  }

  override fun searchV2(pageable: Pageable): Page<UserResponseVO> {
    return queryFactory.pageQuery(UserResponseVO::class.java, pageable) {
      select(
        listOf(
          column(User::id),
          column(User::name),
          column(User::username)))
      from(User::class.java)
    }
  }

  override fun get(id: String): User? {
    return queryFactory.singleQuery {
      select(entity(User::class))
      from(entity(User::class))
      where(col(User::id).equal(id))
    }
  }
}