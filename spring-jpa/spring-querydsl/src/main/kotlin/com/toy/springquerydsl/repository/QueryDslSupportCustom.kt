package com.toy.springquerydsl.repository

import com.querydsl.core.types.Expression
import com.querydsl.core.types.dsl.Wildcard
import com.querydsl.jpa.impl.JPAQuery
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

abstract class QueryDslSupportCustom(
  domainClass: Class<*>
): QuerydslRepositorySupport(domainClass) {

  fun <T> getPage(
    query: JPAQuery<out Any>,
    selectClause: Expression<T>,
    pageable: Pageable
  ): PageImpl<T> {
    val totalCount = query.select(Wildcard.count).fetchFirst() ?: 0L
    val content = querydsl!!.applyPagination(pageable, query.select(selectClause)).fetch()

    return PageImpl(content, pageable, totalCount)
  }
}
