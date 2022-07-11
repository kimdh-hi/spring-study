package com.toy.kotlinjdsl.repository

import com.linecorp.kotlinjdsl.query.spec.predicate.PredicateSpec
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.querydsl.expression.column
import com.linecorp.kotlinjdsl.querydsl.from.join
import com.linecorp.kotlinjdsl.spring.data.SpringDataQueryFactory
import com.linecorp.kotlinjdsl.spring.data.listQuery
import com.linecorp.kotlinjdsl.spring.data.singleQuery
import com.toy.kotlinjdsl.domain.Company
import com.toy.kotlinjdsl.domain.Role
import com.toy.kotlinjdsl.domain.User
import com.toy.kotlinjdsl.vo.UserResponseVO
import com.toy.kotlinjdsl.vo.UserSearchVO
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
}

@Repository
class UserQueryImpl(
  private val queryFactory: SpringDataQueryFactory
): UserQuery {

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
      where(
        searchVO.keyword?.let {
          col(User::name).like("%$it%").or(col(User::username).like("$it%")) } ?: PredicateSpec.empty
      )
      where(
        searchVO.roleId?.let { col(Role::id).equal(it) } ?: PredicateSpec.empty
      )
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