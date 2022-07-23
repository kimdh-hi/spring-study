package com.toy.reactivejdsl.common

import java.io.Serial
import java.io.Serializable

abstract class BaseVO: Serializable {
  companion object {
    @Serial
    private const val serialVersionUID: Long = -7553659470220147916L
  }
}

data class ExistsVO(
  val id: String
): BaseVO() {
  companion object {
    @Serial
    private const val serialVersionUID: Long = 7198078989085325465L
  }
}