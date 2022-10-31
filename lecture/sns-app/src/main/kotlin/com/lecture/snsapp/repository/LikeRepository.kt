package com.lecture.snsapp.repository

import com.lecture.snsapp.domain.Like
import com.lecture.snsapp.domain.Post
import com.lecture.snsapp.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface LikeRepository: JpaRepository<Like, String> {

  fun findByUserAndPost(user: User, post: Post): Like?

  fun findAllByPost(post: Post): List<Like>

  fun countByPost(post: Post): Long
}