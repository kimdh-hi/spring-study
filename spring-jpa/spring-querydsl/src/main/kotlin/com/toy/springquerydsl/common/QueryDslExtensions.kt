package com.toy.springquerydsl.common

import com.querydsl.core.QueryModifiers
import com.querydsl.core.types.dsl.Wildcard
import com.querydsl.jpa.impl.JPAQuery
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils

fun <T> JPAQuery<T>.fetchPaged(pageable: Pageable): Page<T> {
  val countQuery = getCountQuery(this)
  val contentQuery = this.offset(pageable.offset).limit(pageable.pageSize.toLong())
  return PageableExecutionUtils.getPage(contentQuery.fetch(), pageable) { (countQuery.fetchFirst() ?: 0L) }
}

private fun <T> getCountQuery(query: JPAQuery<T>): JPAQuery<Long> {
  return query.clone().apply {
    this.metadata.modifiers = QueryModifiers.EMPTY // clear offset, limit
    this.metadata.clearOrderBy()
    this.metadata.isDistinct = false
  }.select(Wildcard.count)
}
