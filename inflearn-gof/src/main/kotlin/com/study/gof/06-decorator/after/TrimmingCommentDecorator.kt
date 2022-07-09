package com.study.gof.`06-decorator`.after

class TrimmingCommentDecorator(commentService: CommentService): CommentDecorator(commentService) {

  override fun addComment(comment: String) {
    commentService.addComment(comment.trim())
  }
}