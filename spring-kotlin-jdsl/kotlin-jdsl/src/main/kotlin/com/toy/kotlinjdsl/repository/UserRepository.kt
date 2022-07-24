package com.toy.kotlinjdsl.repository

import com.linecorp.kotlinjdsl.query.spec.predicate.PredicateSpec
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.querydsl.expression.column
import com.linecorp.kotlinjdsl.querydsl.from.fetch
import com.linecorp.kotlinjdsl.querydsl.from.join
import com.linecorp.kotlinjdsl.querydsl.where.WhereDsl
import com.linecorp.kotlinjdsl.spring.data.SpringDataQueryFactory
import com.linecorp.kotlinjdsl.spring.data.listQuery
import com.linecorp.kotlinjdsl.spring.data.selectQuery
import com.linecorp.kotlinjdsl.spring.data.singleQuery
import com.toy.kotlinjdsl.domain.Company
import com.toy.kotlinjdsl.domain.Role
import com.toy.kotlinjdsl.domain.User
import com.toy.kotlinjdsl.vo.ExistsVO
import com.toy.kotlinjdsl.vo.UserResponseVO
import com.toy.kotlinjdsl.vo.UserSearchVO
import org.hibernate.SessionFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

interface UserRepository: CrudRepository<User, String>, UserQuery {
  fun findAll(pageable: Pageable): Page<User>
}

interface UserQuery {
  fun searchV0(pageable: Pageable): List<User>
  fun searchV1(pageable: Pageable): Page<User>
  fun searchV2(pageable: Pageable): Page<UserResponseVO>
  fun searchV3(searchVO: UserSearchVO, pageable: Pageable): Page<UserResponseVO>

  fun get(id: String): User?
  fun findByUsername(username: String): User?
  fun existsByUsername(username: String): Boolean
}

@Repository
class UserQueryImpl(
  private val sessionFactory: SessionFactory,
  private val queryFactory: SpringDataQueryFactory
): UserQuery {

  override fun findByUsername(username: String): User? {
    return queryFactory.singleQuery {
      select(entity(User::class))
      from(entity(User::class))
      join(User::company)
      fetch(User::company)
      join(User::role)
      fetch(User::role)
      where(col(User::username).equal(username))
    }
  }

  override fun searchV0(pageable: Pageable): List<User> {
    return queryFactory.listQuery {
      select(entity(User::class))
      from(entity(User::class))
    }
  }

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
          column(User::username),
          column(Company::name),
          column(Role::name)
        ))
      from(entity(User::class))
      join(User::company)
      join(User::role)
    }
  }

  override fun searchV3(searchVO: UserSearchVO, pageable: Pageable): Page<UserResponseVO> {

    return queryFactory.pageQuery(UserResponseVO::class.java, pageable) {
      select(listOf(
          column(User::id),
          column(User::name),
          column(User::username),
          column(Company::name),
          column(Role::name)))
      from(entity(User::class))
      join(User::company)
      join(User::role)
      where(searchCondition(searchVO))
    }
  }

  private fun WhereDsl.searchCondition(searchVO: UserSearchVO): PredicateSpec {
    return and(
      searchVO.roleId?.let { col(Role::id).equal(it) } ?: PredicateSpec.empty
    ).and(
      searchVO.keyword?.let {
        col(User::name).like("%$it%").or(col(User::username).like("$it%"))
      } ?: PredicateSpec.empty
    )
  }

  override fun get(id: String): User? {
    return queryFactory.singleQuery {
      select(entity(User::class))
      from(entity(User::class))
      join(User::company)
      fetch(User::company)
      join(User::role)
      fetch(User::role)
      where(col(User::id).equal(id))
    }
  }

  override fun existsByUsername(username: String): Boolean {
    val result = queryFactory.selectQuery<ExistsVO> {
      select(listOf(column(User::id)))
      from(entity(User::class))
      where(col(User::username).equal(username))
      limit(1)
    }

    return result.resultList.isNotEmpty()
  }
}