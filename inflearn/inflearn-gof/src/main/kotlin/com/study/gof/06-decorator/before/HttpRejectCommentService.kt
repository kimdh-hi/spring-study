package com.study.gof.`06-decorator`.before

class HttpRejectCommentService: CommentService() {

  override fun addComment(comment: String) {
    if(check(comment))
      println(comment)
  }

  private fun check(comment: String) = !comment.contains("http")

}