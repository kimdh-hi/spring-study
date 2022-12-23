package com.toy.springredischat.vo

import java.io.Serial
import java.io.Serializable

data class MessageVO(
  val id: String? = null,
  val sender: String,
  val message: String
): Serializable {
  companion object {
    @Serial
    private const val serialVersionUID: Long = -7252900717623444066L
  }
}