package com.study.hexagonal.board.application.domain

data class Comment(
  val id: Long? = null,
  val authorId: Long,
  val content: String
) {
  init {
    require(content.isNotBlank()) { "댓글 내용은 필수입니다" }
    require(content.length <= 500) { "댓글은 500자를 넘을 수 없습니다" }
  }
}
