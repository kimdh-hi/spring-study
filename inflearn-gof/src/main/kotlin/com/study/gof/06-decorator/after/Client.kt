package com.study.gof.`06-decorator`.after

import com.study.gof.`06-decorator`.before.Client

class Client(
  private val commentService: CommentService
) {

  fun write(comment: String) = commentService.addComment(comment)
}

fun main() {
  val enabledTrimming = true
  val enabledFiltering = true

  var commentService: CommentService = CommentServiceImpl()

  if (enabledFiltering) commentService = FilteringCommentDecorator(commentService)
  if (enabledTrimming) commentService = TrimmingCommentDecorator(commentService)

  val client = Client(commentService)
  client.write("comment1..")
  client.write("    comment2..")
  client.write("http://localhost:8080")
}