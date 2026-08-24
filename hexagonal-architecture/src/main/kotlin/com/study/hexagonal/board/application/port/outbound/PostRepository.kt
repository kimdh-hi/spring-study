package com.study.hexagonal.board.application.port.outbound

import com.study.hexagonal.board.application.domain.Post

interface PostRepository {
  fun save(post: Post): Post
  fun delete(post: Post)
  fun findById(postId: Long): Post?
  fun findAll(): List<Post>
}
