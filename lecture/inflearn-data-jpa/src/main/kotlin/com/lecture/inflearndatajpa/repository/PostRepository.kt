package com.lecture.inflearndatajpa.repository

import com.lecture.inflearndatajpa.domain.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository: JpaRepository<Post, Long>, PostCustomRepository<Post> {
  fun findByTitleContains(title: String, pageable: Pageable): Page<Post>
  fun countByTitleContains(title: String): Long
}