package com.study.gof.`06-decorator`.before

open class CommentService() {

  open fun addComment(comment: String) {
    println(comment)
  }
}