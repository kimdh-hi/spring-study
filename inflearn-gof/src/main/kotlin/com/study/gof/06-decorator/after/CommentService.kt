package com.study.gof.`06-decorator`.after

interface CommentService {

  fun addComment(comment: String)
}

open class CommentServiceImpl: CommentService {
  override fun addComment(comment: String) {
    println(comment)
  }
}