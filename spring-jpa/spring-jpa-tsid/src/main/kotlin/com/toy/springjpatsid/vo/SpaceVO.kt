package com.toy.springjpatsid.vo

import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime

data class SpaceSearchVO(
  val searchTest: String? = null,
  override var cursorId: String? = null,
) : AbstractCursorPagingRequestVO()

data class SpaceResponseVO @QueryProjection constructor(
  val id: String,
  val name: String,
  val createdDate: LocalDateTime?
)