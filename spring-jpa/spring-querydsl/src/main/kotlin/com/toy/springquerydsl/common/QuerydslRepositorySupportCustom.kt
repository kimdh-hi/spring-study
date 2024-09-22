package com.toy.springquerydsl.common

import com.querydsl.core.types.Expression
import com.querydsl.core.types.dsl.Wildcard
import com.querydsl.jpa.impl.JPAQuery
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.SliceImpl
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class QuerydslRepositorySupportCustom(domainClass: Class<*>) : QuerydslRepositorySupport(domainClass) {

  fun <T> getPage(
    query: JPAQuery<out Any>,
    selectClause: Expression<T>,
    pageable: Pageable
  ): PageImpl<T> {
    val totalCount = query.select(Wildcard.count).fetchFirst() ?: 0L
    val list = querydsl!!.applyPagination(pageable, query.select(selectClause)).fetch()

    return PageImpl(list, pageable, totalCount)
  }

  fun <T> getSlice(
    query: JPAQuery<out T>,
    pageable: Pageable
  ): SliceImpl<T> {
    val list = query
      .offset(pageable.offset)
      .limit((pageable.pageSize + 1).toLong())
      .fetch()

    return SliceImpl(list, pageable, hasNext(list, pageable.pageSize))
  }

  fun <T> getSlice(
    list: MutableList<T>,
    pageable: Pageable
  ): SliceImpl<T> {
    return SliceImpl(list, pageable, hasNext(list, pageable.pageSize))
  }

  private fun <T> hasNext(list: MutableList<T>, pageSize: Int): Boolean {
    return if (list.size > pageSize) {
      list.removeAt(pageSize)
      true
    } else {
      false
    }
  }
}
