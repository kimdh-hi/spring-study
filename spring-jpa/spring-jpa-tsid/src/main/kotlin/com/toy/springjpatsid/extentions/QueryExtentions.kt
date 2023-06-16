package com.toy.springjpatsid.extentions

import com.querydsl.core.QueryModifiers
import com.querydsl.jpa.impl.JPAQuery
import com.toy.springjpatsid.vo.SliceResponse

fun <T> JPAQuery<T>.fetchSlice(pageSize: Int): SliceResponse<T> {
  this.metadata.modifiers = QueryModifiers.EMPTY

  val list = this.limit(pageSize.toLong() + 1)
    .fetch()

  val hasNext = list.size > pageSize
  if (hasNext) list.removeLast()
  return SliceResponse(list, hasNext)
}
