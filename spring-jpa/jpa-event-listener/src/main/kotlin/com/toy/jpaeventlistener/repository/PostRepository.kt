package com.toy.jpaeventlistener.repository

import com.toy.jpaeventlistener.domain.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository: JpaRepository<Post, Long> {
}