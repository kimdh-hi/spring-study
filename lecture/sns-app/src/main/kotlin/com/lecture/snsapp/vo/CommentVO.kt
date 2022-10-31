package com.lecture.snsapp.vo

import com.lecture.snsapp.domain.Comment
import java.sql.Timestamp

data class CommentRequestVO(
  val comment: String
)

data class CommentResponseVO(
  val id: String,
  val postId: String,
  val username: String,
  val comment: String,
  val registeredAt: Timestamp
) {
  companion object {
    fun of(comment: Comment) = CommentResponseVO(
      id = comment.id,
      postId = comment.post.id,
      username = comment.user.username,
      comment = comment.comment,
      registeredAt = comment.registerAt!!
    )
  }
}