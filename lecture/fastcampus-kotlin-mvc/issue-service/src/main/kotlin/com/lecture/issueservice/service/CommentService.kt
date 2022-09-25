package com.lecture.issueservice.service

import com.lecture.issueservice.domain.Comment
import com.lecture.issueservice.exception.NotFoundException
import com.lecture.issueservice.model.CommentRequest
import com.lecture.issueservice.model.CommentResponse
import com.lecture.issueservice.repository.CommentRepository
import com.lecture.issueservice.repository.IssueRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CommentService(
  private val commentRepository: CommentRepository,
  private val issueRepository: IssueRepository
) {
  @Transactional
  fun create(issueId: Long, userId: Long, username: String, request: CommentRequest): CommentResponse {
    val issue = issueRepository.findByIdOrNull(issueId) ?: throw NotFoundException("issue not found...")
    val comment = Comment(
      issue = issue,
      userId = userId,
      username = username,
      body = request.body
    )
    issue.comments.add(comment)
    return CommentResponse(commentRepository.save(comment))
  }

  @Transactional
  fun edit(issueId: Long, commentId: Long, userId: Long, request: CommentRequest) {
    commentRepository.findByIdAndUserId(commentId, userId)?.run {
      body = request.body
      commentRepository.save(this)
    } ?: throw NotFoundException("comment not found...")
  }

  @Transactional
  fun delete(issueId: Long, commentId: Long, userId: Long) {
    val issue = issueRepository.findByIdOrNull(issueId) ?: throw NotFoundException("issue not found...")
    commentRepository.findByIdAndUserId(commentId, userId)?.let {
      issue.comments.remove(it)
    }
  }
}