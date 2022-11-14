package com.lecture.fsmysql.domain.post.entity

import java.time.LocalDateTime

class Timeline(
  var id: Long = 0L,

  val memberId: Long,

  val postId: Long,

  var createdAt: LocalDateTime = LocalDateTime.now()
) {
  override fun toString(): String {
    return "Timeline(id=$id, memberId=$memberId, postId=$postId, createdAt=$createdAt)"
  }
}