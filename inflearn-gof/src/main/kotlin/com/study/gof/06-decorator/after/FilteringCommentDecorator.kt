package com.study.gof.`06-decorator`.after

class FilteringCommentDecorator(
  commentService: CommentService
): CommentDecorator(commentService) {

  override fun addComment(comment: String) {
    if(check(comment))
      println(comment)
  }

  private fun check(comment: String) = !comment.contains("http")
}