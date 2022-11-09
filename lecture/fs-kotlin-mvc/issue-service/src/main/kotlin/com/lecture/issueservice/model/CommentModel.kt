package com.lecture.issueservice.model

import com.lecture.issueservice.domain.Comment

data class CommentRequest(
  val body: String
)

data class CommentResponse(
  val id: Long,
  val issueId: Long,
  val userId: Long,
  val username: String,
  val body: String
) {
  companion object {
    operator fun invoke(comment: Comment) = with(comment) {
      CommentResponse(
        id = id!!,
        issueId = issue.id!!,
        userId = userId,
        username = username,
        body = body
      )
    }
  }
}