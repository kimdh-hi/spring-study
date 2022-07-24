package com.study.gof.`06-decorator`.after

open class CommentDecorator(
  val commentService: CommentService
): CommentService {

  override fun addComment(comment: String) {
    commentService.addComment(comment)
  }
}