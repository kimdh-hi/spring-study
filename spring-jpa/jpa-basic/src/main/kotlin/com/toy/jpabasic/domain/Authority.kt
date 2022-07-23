package com.toy.jpabasic.domain

import com.querydsl.core.types.dsl.CaseBuilder
import com.querydsl.core.types.dsl.EnumPath
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.core.types.dsl.StringExpression
import com.toy.jpabasic.common.constants.AuthorityConstants

enum class Authority {

  USER, ADMIN;

  fun getRole() =
    when(this) {
      ADMIN -> AuthorityConstants.ROLE_ADMIN
      USER -> AuthorityConstants.ROLE_USER
    }
}

object AuthorityEnumPathSupporter {
  val enumPath: EnumPath<Authority> = Expressions.enumPath(Authority::class.java, "authorities")

  val authorityCaseBuilder: StringExpression = CaseBuilder()
    .`when`(enumPath.eq(Authority.ADMIN)).then(Authority.ADMIN.name)
    .otherwise(Authority.USER.name)
}
