package com.study.gof.`06-decorator`.before

class TrimmingCommentService(): CommentService() {

  override fun addComment(comment: String) {
    println(comment.trim())
  }
}