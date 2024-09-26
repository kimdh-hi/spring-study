package com.toy.springquerydsl.common

import com.querydsl.core.QueryModifiers
import com.querydsl.core.types.dsl.SimpleExpression
import com.querydsl.core.types.dsl.Wildcard
import com.querydsl.jpa.impl.JPAQuery
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import java.util.function.LongSupplier

fun <T> JPAQuery<T>.fetchPaged(pageable: Pageable, countExpression: SimpleExpression<*>): Page<T> {
  val contentQuery = this.offset(pageable.offset).limit(pageable.pageSize.toLong())
  return PageableExecutionUtils.getPage(contentQuery.fetch(), pageable, countQuery(this, countExpression))
}

private fun <T> countQuery(query: JPAQuery<T>, countExpression: SimpleExpression<*>) = LongSupplier {
  query.clone().apply {
    this.metadata.modifiers = QueryModifiers.EMPTY
    this.metadata.clearOrderBy()
  }.select(countExpression.countDistinct())
    .fetchFirst() ?: 0L
}


