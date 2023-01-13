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

  override fun toString(): String {
    return "TestVO(data='$data')"
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as TestVO

    if (data != other.data) return false

    return true
  }

  override fun hashCode(): Int {
    return data.hashCode()
  }

}