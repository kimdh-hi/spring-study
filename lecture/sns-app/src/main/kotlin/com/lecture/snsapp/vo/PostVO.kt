package com.lecture.snsapp.vo

import com.lecture.snsapp.common.NoArg
import com.lecture.snsapp.domain.Post

data class PostCreateRequestVO(
  val title: String,
  val body: String
)

@NoArg
data class PostCreateResponseVO(
  val id: String,
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

data class PostModifyRequestVO(
  val title: String,
  val body: String
)
