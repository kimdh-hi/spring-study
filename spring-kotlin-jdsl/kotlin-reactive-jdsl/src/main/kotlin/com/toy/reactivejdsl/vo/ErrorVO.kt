package com.toy.reactivejdsl.vo

import com.toy.reactivejdsl.common.BaseVO
import java.io.Serial

data class ErrorVO(
  val errorCode: String,
  val message: String
): BaseVO() {
  companion object {
    @Serial
    private const val serialVersionUID: Long = 5224102776637566092L
  }
}