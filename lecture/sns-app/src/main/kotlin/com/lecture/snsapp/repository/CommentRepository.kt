package com.lecture.snsapp.repository

import com.lecture.snsapp.domain.Comment
import com.lecture.snsapp.domain.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository: JpaRepository<Comment, String> {
  fun findAllByPost(post: Post, pageable: Pageable): Page<Comment>
}