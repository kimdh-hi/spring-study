package com.lecture.fsmysql.common

data class PageCursor<T>(
  val nextCursorRequest: CursorRequest,
  val body: List<T>
)