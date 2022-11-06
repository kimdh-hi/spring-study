package com.lecture.fsmysql.domain.post.dto

data class PostCommand(
  val memberId: Long,
  val content: String
)