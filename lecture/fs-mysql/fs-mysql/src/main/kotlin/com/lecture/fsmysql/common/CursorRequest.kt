package com.lecture.fsmysql.common

data class CursorRequest(
  val key: Long? = null,
  val size: Long
) {

  companion object {
    const val DEFAULT_KEY = -1L
  }

  fun hasKey() = key != null

  fun next(key: Long): CursorRequest {
    return CursorRequest(key, size)
  }
}