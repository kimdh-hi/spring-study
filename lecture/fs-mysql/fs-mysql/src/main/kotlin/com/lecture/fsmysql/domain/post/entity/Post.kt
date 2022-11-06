package com.lecture.fsmysql.domain.post.entity

import java.time.LocalDateTime

class Post(
  var id: Long? = null,

  val memberId: Long,

  val content: String,

  var createdAt: LocalDateTime = LocalDateTime.now()
)