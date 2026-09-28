package com.study.jpacore.repository

import com.study.jpacore.entity.Post
import org.springframework.data.domain.Limit
import org.springframework.data.domain.ScrollPosition
import org.springframework.data.domain.Window
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository : JpaRepository<Post, Long> {
  fun findTop3ByOrderByIdAsc(position: ScrollPosition): Window<Post>

  fun findFirst3ByOrderByIdAsc(position: ScrollPosition): Window<Post>

  fun findByOrderByIdAsc(position: ScrollPosition, limit: Limit): Window<Post>
}
