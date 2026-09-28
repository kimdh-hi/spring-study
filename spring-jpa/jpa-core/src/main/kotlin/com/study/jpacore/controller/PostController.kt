package com.study.jpacore.controller

import com.study.jpacore.entity.Post
import com.study.jpacore.repository.PostRepository
import org.springframework.data.domain.Limit
import org.springframework.data.domain.ScrollPosition
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/posts")
class PostController(
  private val postRepository: PostRepository,
) {

  @GetMapping
  fun scroll(
    @RequestParam(required = false) cursor: Long?,
    @RequestParam(defaultValue = "3") size: Int,
  ): CursorResponse<PostResponse> {
    val position = cursor?.let { ScrollPosition.forward(mapOf("id" to it)) } ?: ScrollPosition.keyset()
    val window = postRepository.findByOrderByIdAsc(position, Limit.of(size))
    val nextCursor = if (window.hasNext()) window.last().id else null
    return CursorResponse(window.content.map(PostResponse::from), nextCursor)
  }
}

data class CursorResponse<T>(
  val items: List<T>,
  val nextCursor: Long?,
)

data class PostResponse(
  val id: Long,
  val title: String,
) {
  companion object {
    fun from(post: Post) = PostResponse(id = post.id!!, title = post.title)
  }
}
