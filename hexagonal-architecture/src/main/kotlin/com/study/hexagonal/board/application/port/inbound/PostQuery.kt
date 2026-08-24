package com.study.hexagonal.board.application.port.inbound

import com.study.hexagonal.board.application.domain.Post

interface PostQuery {
  fun find(postId: Long): Post
  fun findAll(): List<Post>
}
