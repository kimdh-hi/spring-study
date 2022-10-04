package com.lecture.inflearndatajpa.repository

import com.lecture.inflearndatajpa.domain.Comment
import org.springframework.data.repository.RepositoryDefinition

@RepositoryDefinition(domainClass = Comment::class, idClass = Long::class)
interface CommentRepository {

  fun save(comment: Comment): Comment

  fun findAll(): List<Comment>
}