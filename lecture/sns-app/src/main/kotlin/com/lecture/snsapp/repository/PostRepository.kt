package com.lecture.snsapp.repository

import com.lecture.snsapp.domain.Post
import com.lecture.snsapp.domain.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository: JpaRepository<Post, String> {
  fun findAllByUser(user: User, pageable: Pageable): Page<Post>
}