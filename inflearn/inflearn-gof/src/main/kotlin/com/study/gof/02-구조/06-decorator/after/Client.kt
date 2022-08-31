package com.study.gof.`06-decorator`.after

class Client(
  private val commentService: CommentService
) {

  fun write(comment: String) = commentService.addComment(comment)
}

fun main() {
  val enabledTrimming = true
  val enabledFiltering = true

  // 기본적으로 사용될 CommentService
  var commentService: CommentService = CommentServiceImpl()

  // 동적으로 적용될 데코레이터의 대한 처리
  if (enabledFiltering) commentService = FilteringCommentDecorator(commentService)
  if (enabledTrimming) commentService = TrimmingCommentDecorator(commentService)

  val client = Client(commentService)
  client.write("comment1..")
  client.write("    comment2..")
  client.write("http://localhost:8080")
}