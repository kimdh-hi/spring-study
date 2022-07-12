package com.toy.reactivejdsl.repository

import com.linecorp.kotlinjdsl.query.spec.predicate.PredicateSpec
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.querydsl.expression.column
import com.linecorp.kotlinjdsl.querydsl.from.fetch
import com.linecorp.kotlinjdsl.querydsl.from.join
import com.linecorp.kotlinjdsl.querydsl.where.WhereDsl
import com.linecorp.kotlinjdsl.spring.data.reactive.query.SpringDataHibernateMutinyReactiveQueryFactory
import com.linecorp.kotlinjdsl.spring.data.reactive.query.listQuery
import com.linecorp.kotlinjdsl.spring.data.reactive.query.pageQuery
import com.linecorp.kotlinjdsl.spring.data.reactive.query.singleQuery
import com.toy.reactivejdsl.common.ExistsVO
import com.toy.reactivejdsl.domain.Company
import com.toy.reactivejdsl.domain.Role
import com.toy.reactivejdsl.domain.User
import com.toy.reactivejdsl.vo.UserResponseVO
import com.toy.reactivejdsl.vo.UserSearchVO
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.hibernate.reactive.mutiny.Mutiny
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

interface UserRepository {
  suspend fun findById(id: String): User?
  suspend fun findByUsername(username: String): User?
  suspend fun search(pageable: Pageable, searchVO: UserSearchVO): Page<UserResponseVO>
  suspend fun existsByUsername(username: String): Boolean
  suspend fun get(id: String): User?
  suspend fun save(user: User): User
}

@Repository
class UserRepositoryImpl (
  private val sessionFactory: Mutiny.SessionFactory,
  private val queryFactory: SpringDataHibernateMutinyReactiveQueryFactory,
): UserRepository {

  override suspend fun findById(id: String): User? {
    return queryFactory.singleQuery {
      select(entity(User::class))
      from(entity(User::class))
      where(col(User::id).equal(id))
    }
  }

  override suspend fun get(id: String): User? {
    return queryFactory.singleQuery {
      select(entity(User::class))
      from(entity(User::class))
      where(col(User::id).equal(id))
    }
  }

  override suspend fun save(user: User): User {
    return user.also {
      sessionFactory.withSession { session -> session.persist(it).flatMap {
        session.flush()
      } }.awaitSuspending()
    }
  }

  override suspend fun findByUsername(username: String): User? {
    return queryFactory.singleQuery {
      select(entity(User::class))
      from(entity(User::class))
      where(col(User::username).equal(username))
      join(User::company)
      fetch(User::company)
      join(User::role)
      fetch(User::role)
    }
  }

  override suspend fun search(pageable: Pageable, searchVO: UserSearchVO): Page<UserResponseVO> {
    return queryFactory.pageQuery(pageable) {
      select(listOf(
        col(User::id),
        col(User::name),
        col(User::username),
        col(Company::name),
        col(Role::name)
      ))
      from(entity(User::class))
      join(User::company)
      join(User::role)
      where(searchCondition(searchVO))
    }
  }

  override suspend fun existsByUsername(username: String): Boolean {
    val result = queryFactory.listQuery<ExistsVO> {
      select(listOf(column(User::id)))
      from(entity(User::class))
      where(col(User::username).equal(username))
      limit(1)
    }

    return result.isNotEmpty()
  }

  private fun WhereDsl.searchCondition(searchVO: UserSearchVO) =
    and(
      searchVO.roleId?.let { col(Role::id).equal(it) } ?: PredicateSpec.empty
    ).and(
      searchVO.keyword?.let { col(User::name).like("%$it%").or(col(User::username).like("$it%")) } ?: PredicateSpec.empty
    )

}