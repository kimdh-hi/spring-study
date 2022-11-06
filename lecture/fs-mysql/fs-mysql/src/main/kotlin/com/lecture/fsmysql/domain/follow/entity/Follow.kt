package com.lecture.fsmysql.domain.follow.entity

import java.time.LocalDateTime

class Follow(
  var id: Long? = null,
  var fromMemberId: Long,
  var toMemberId: Long,
  var createdAt: LocalDateTime = LocalDateTime.now()
)