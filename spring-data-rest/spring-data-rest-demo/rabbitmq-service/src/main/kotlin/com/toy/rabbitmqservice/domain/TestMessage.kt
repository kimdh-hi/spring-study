package com.toy.rabbitmqservice.domain

import java.io.Serial

data class TestMessage(
  val id: String,
  val data: String
): java.io.Serializable {
  companion object {
    @Serial
    private const val serialVersionUID: Long = -4196122426527676774L
  }
}
