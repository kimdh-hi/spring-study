package com.lecture.fsmysql.domain.post.entity

import java.time.LocalDateTime

class Post(
  var id: Long = 0L,

  val memberId: Long,

  val content: String,

  var createdAt: LocalDateTime = LocalDateTime.now()
) {
  override fun toString(): String {
    return "Post(id=$id, memberId=$memberId, content='$content', createdAt=$createdAt)"
  }
}