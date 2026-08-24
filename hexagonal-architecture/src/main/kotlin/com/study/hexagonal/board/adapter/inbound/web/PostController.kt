package com.study.hexagonal.board.adapter.inbound.web

import com.study.hexagonal.board.application.port.inbound.PostQuery
import com.study.hexagonal.board.application.port.inbound.PostUseCase
import com.study.hexagonal.board.application.domain.Post
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/posts")
class PostController(
  private val postUseCase: PostUseCase,
  private val postQuery: PostQuery
) {

  @PostMapping
  fun write(@RequestBody request: WriteRequest): PostResponse =
    PostResponse.from(postUseCase.write(request.authorId, request.title, request.content))

  @PutMapping("/{postId}")
  fun edit(@PathVariable postId: Long, @RequestBody request: EditRequest): PostResponse =
    PostResponse.from(postUseCase.edit(postId, request.editorId, request.title, request.content))

  @DeleteMapping("/{postId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  fun delete(@PathVariable postId: Long, @RequestParam requesterId: Long) =
    postUseCase.delete(postId, requesterId)

  @GetMapping("/{postId}")
  fun find(@PathVariable postId: Long): PostResponse =
    PostResponse.from(postQuery.find(postId))

  @GetMapping
  fun findAll(): List<PostResponse> = postQuery.findAll().map { PostResponse.from(it) }

  @PostMapping("/{postId}/comments")
  fun comment(@PathVariable postId: Long, @RequestBody request: CommentRequest): PostResponse =
    PostResponse.from(postUseCase.comment(postId, request.authorId, request.content))

  @DeleteMapping("/{postId}/comments/{commentId}")
  fun deleteComment(
    @PathVariable postId: Long,
    @PathVariable commentId: Long,
    @RequestParam requesterId: Long
  ): PostResponse = PostResponse.from(postUseCase.deleteComment(postId, commentId, requesterId))

  data class WriteRequest(val authorId: Long, val title: String, val content: String)

  data class EditRequest(val editorId: Long, val title: String, val content: String)

  data class CommentRequest(val authorId: Long, val content: String)

  data class PostResponse(
    val id: Long,
    val authorId: Long,
    val title: String,
    val content: String,
    val comments: List<CommentResponse>
  ) {
    companion object {
      fun from(post: Post) = PostResponse(
        id = post.id!!,
        authorId = post.authorId,
        title = post.title,
        content = post.content,
        comments = post.comments.map { CommentResponse(it.id!!, it.authorId, it.content) }
      )
    }
  }

  data class CommentResponse(val id: Long, val authorId: Long, val content: String)
}
