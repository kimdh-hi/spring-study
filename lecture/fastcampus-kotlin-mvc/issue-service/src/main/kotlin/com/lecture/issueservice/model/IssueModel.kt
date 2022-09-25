package com.lecture.issueservice.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.lecture.issueservice.domain.Comment
import com.lecture.issueservice.domain.Issue
import com.lecture.issueservice.domain.enums.IssuePriority
import com.lecture.issueservice.domain.enums.IssueStatus
import com.lecture.issueservice.domain.enums.IssueType
import java.time.LocalDateTime

data class IssueRequest(
  val summary: String,
  val description: String,
  val type: IssueType,
  val priority: IssuePriority,
  val status: IssueStatus
)

data class IssueResponse(
  val id: Long,
  val summary: String,
  val description: String,
  val type: IssueType,
  val priority: IssuePriority,
  val status: IssueStatus,
  val userId: Long,
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  val createdAt: LocalDateTime?,
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  val updatedAt: LocalDateTime?,

  val comments: List<CommentResponse> = emptyList()
) {
  companion object {
    operator fun invoke(issue: Issue) = with(issue) {
      IssueResponse(
        id = id!!,
        summary = summary,
        description = description,
        type = type,
        priority = priority,
        status = status,
        userId = userId,
        createdAt = createdAt,
        updatedAt = updatedAt,

        comments = comments.sortedByDescending(Comment::createdAt).map { CommentResponse(it) }
      )
    }
  }
}