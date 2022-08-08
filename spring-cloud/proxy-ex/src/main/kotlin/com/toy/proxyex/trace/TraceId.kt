package com.toy.proxyex.trace

import java.util.UUID

data class TraceId(
  val id: String = UUID.randomUUID().toString().substring(0, 8),
  val level: Int = 0
) {
  fun createNextId() = TraceId(id, level+1)
  fun createPreviousId() = TraceId(id, level-1)
  fun isFirstLevel() = level == 0

}