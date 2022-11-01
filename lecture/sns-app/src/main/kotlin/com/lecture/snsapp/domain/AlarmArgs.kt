package com.lecture.snsapp.domain

import java.io.Serial
import java.io.Serializable

data class AlarmArgs(
  val fromUserId: String,
  val targetId: String,
): Serializable {
  companion object {
    @Serial
    private const val serialVersionUID: Long = -28164797814931120L
  }
}