package com.toy.selfjoinquery.repository

import com.querydsl.core.types.Expression
import com.querydsl.core.types.dsl.Wildcard
import com.querydsl.jpa.impl.JPAQuery
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class QuerydslRepositorySupportCustom(domainClass: Class<*>) : QuerydslRepositorySupport(domainClass) {

  fun <T> getPage(
    query: JPAQuery<out Any>,
    selectClause: Expression<T>,
    pageable: Pageable
  ): PageImpl<T> {
    val totalCount = query.select(Wildcard.count).fetchFirst()
    val list = querydsl!!.applyPagination(pageable, query.select(selectClause)).fetch()

    return PageImpl(list, pageable, totalCount)
  }

}