package com.toy.springjpatsid.vo

import com.fasterxml.jackson.annotation.JsonIgnore
import com.querydsl.core.types.Order
import org.springframework.data.domain.Pageable
import org.springframework.data.querydsl.QPageRequest
import org.springframework.data.querydsl.QSort

abstract class AbstractCursorPagingRequestVO(
  open val cursorId: String? = null,
  open val size: Int = DEFAULT_PAGE_SIZE,
) {

  companion object {
    const val DEFAULT_PAGE_SIZE: Int = 5
  }
}

data class SliceResponse<T>(
  val content: List<T>,
  val hasNext: Boolean,
)