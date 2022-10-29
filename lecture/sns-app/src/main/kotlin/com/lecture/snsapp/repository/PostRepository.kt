package com.lecture.snsapp.repository

import com.lecture.snsapp.domain.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository: JpaRepository<Post, String> {
}