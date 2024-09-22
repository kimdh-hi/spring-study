package com.toy.springquerydsl.common

import com.querydsl.jpa.impl.JPAQuery
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils

fun <T> JPAQuery<T>.fetchPage(pageable: Pageable, countJPAQuery: JPAQuery) {
  PageableExecutionUtils.getPage(fetch(), pageable) { this.fetchFirst() ?: 0L }
}
