package com.lecture.snsapp.vo

import com.lecture.snsapp.domain.Post

data class PostCreateRequestVO(
  val title: String,
  val body: String
)

data class PostCreateResponseVO(
  val id: Long,
  val title: String,
  val writer: String
) {
  companion object {
    fun of(post: Post) = PostCreateResponseVO(
      id = post.id!!,
      title = post.title,
      writer = post.user.username
    )
  }
}