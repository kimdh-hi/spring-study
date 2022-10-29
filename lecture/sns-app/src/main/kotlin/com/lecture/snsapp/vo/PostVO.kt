package com.lecture.snsapp.vo

import com.lecture.snsapp.common.NoArg
import com.lecture.snsapp.domain.Post
import java.time.LocalDateTime

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

data class PostResponseVO(
  val id: String,
  val title: String,
  val body: String,
  val registerAt: LocalDateTime?,
) {
  companion object {
    fun of(post: Post) = PostResponseVO(
      id = post.id,
      title = post.title,
      body = post.body,
      registerAt = post.registerAt?.toLocalDateTime()
    )
  }
}
