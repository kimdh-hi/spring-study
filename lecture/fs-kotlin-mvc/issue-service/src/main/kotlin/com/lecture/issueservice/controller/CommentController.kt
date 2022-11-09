package com.lecture.issueservice.controller

import com.lecture.issueservice.config.AuthUser
import com.lecture.issueservice.model.CommentRequest
import com.lecture.issueservice.model.CommentResponse
import com.lecture.issueservice.service.CommentService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/issues/{issueId}/comments")
class CommentController(
  private val commentService: CommentService
) {

  @PostMapping
  fun create(
    authUser: AuthUser,
    @PathVariable issueId: Long,
    @RequestBody request: CommentRequest
  ): ResponseEntity<CommentResponse> {
    val response = commentService.create(issueId, authUser.userId, authUser.username, request)
    return ResponseEntity.ok(response)
  }

  @PutMapping("/{commentId}")
  fun edit(
    authUser: AuthUser,
    @PathVariable issueId: Long,
    @PathVariable commentId: Long,
    @RequestBody request: CommentRequest
  ): ResponseEntity<Unit> {
    commentService.edit(issueId, commentId, authUser.userId, request)
    return ResponseEntity.noContent().build()
  }

  @DeleteMapping("/{commentId}")
  fun delete(
    authUser: AuthUser,
    @PathVariable issueId: Long,
    @PathVariable commentId: Long
  ): ResponseEntity<Unit> {
    commentService.delete(issueId, commentId, authUser.userId)
    return ResponseEntity.noContent().build()
  }
}