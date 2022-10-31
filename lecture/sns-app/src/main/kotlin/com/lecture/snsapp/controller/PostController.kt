package com.lecture.snsapp.controller

import com.lecture.snsapp.common.Response
import com.lecture.snsapp.service.PostService
import com.lecture.snsapp.vo.*
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/posts")
class PostController(
  private val postService: PostService
) {
  private val log = LoggerFactory.getLogger(javaClass)

  @GetMapping
  fun list(
    pageable: Pageable,
    authentication: Authentication
  ): Response<Page<PostResponseVO>> {
    val responseVO = postService.list(pageable)
    return Response.success(result = responseVO)
  }

  @GetMapping("/me")
  fun myList(
    pageable: Pageable,
    authentication: Authentication
  ): Response<Page<PostResponseVO>> {
    val user = authentication.principal as User
    val responseVO = postService.myList(user.username, pageable)
    return Response.success(result = responseVO)
  }

  @PostMapping
  fun create(
    @RequestBody requestVO: PostCreateRequestVO,
    authentication: Authentication
  ): Response<PostCreateResponseVO> {
    val user = authentication.principal as User
    val responseVO = postService.create(requestVO.title, requestVO.body, user.username)
    return Response.success(result = responseVO)
  }

  @PutMapping("/{id}")
  fun modify(
    @PathVariable id: String, @RequestBody requestVO: PostModifyRequestVO,
    authentication: Authentication
  ): Response<Unit> {
    val user = authentication.principal as User
    postService.modify(id, requestVO.title, requestVO.body, user.username)
    return Response.success()
  }

  @DeleteMapping("/{id}")
  fun delete(
    @PathVariable id: String,
    authentication: Authentication
  ): Response<Unit> {
    val user = authentication.principal as User
    postService.delete(id, user.username)
    return Response.success()
  }

  @PostMapping("/{id}/likes")
  fun likes(
    @PathVariable id: String,
    authentication: Authentication
  ): Response<Unit> {
    val user = authentication.principal as User
    postService.like(id, user.username)
    return Response.success()
  }

  @GetMapping("/{id}/likes/count")
  fun likeCount(
    @PathVariable id: String,
    authentication: Authentication
  ): Response<Long> {
    val count = postService.likeCount(id)
    return Response.success(result = count)
  }

  @PostMapping("/{id}/comments")
  fun addComment(
    @PathVariable id: String,
    authentication: Authentication,
    @RequestBody requestVO: CommentRequestVO
  ): Response<Unit> {
    val user = authentication.principal as User
    postService.addComment(id, user.username, requestVO)
    return Response.success()
  }

  @GetMapping("/{id}/comments")
  fun getComments(
    @PathVariable id: String,
    authentication: Authentication,
    pageable: Pageable
  ): Response<Page<CommentResponseVO>> {
    val responseVO = postService.getComments(id, pageable)
    return Response.success(result = responseVO)
  }
}