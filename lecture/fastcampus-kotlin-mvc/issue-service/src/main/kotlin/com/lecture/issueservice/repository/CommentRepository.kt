package com.lecture.issueservice.repository

import com.lecture.issueservice.domain.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository: JpaRepository<Comment, Long> {
  fun findByIdAndUserId(commentId: Long, userId: Long): Comment?
}