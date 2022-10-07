package com.toy.springehcache.vo

import java.io.Serial
import java.io.Serializable

data class TestVO(
  val data: String = "data..."
): Serializable {
  companion object {
    @Serial
    private const val serialVersionUID: Long = -8022471037423401526L
  }
}