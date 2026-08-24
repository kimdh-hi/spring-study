package com.study.hexagonal.board.application.port.inbound

import com.study.hexagonal.board.application.domain.Post

interface PostUseCase {
  fun write(authorId: Long, title: String, content: String): Post
  fun edit(postId: Long, editorId: Long, title: String, content: String): Post
  fun delete(postId: Long, requesterId: Long)
  fun comment(postId: Long, authorId: Long, content: String): Post
  fun deleteComment(postId: Long, commentId: Long, requesterId: Long): Post
}
