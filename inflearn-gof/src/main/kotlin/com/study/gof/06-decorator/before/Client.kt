package com.study.gof.`06-decorator`.before

class Client(
  private val commentService: CommentService
) {
  fun write(comment: String) {
    commentService.addComment(comment)
  }
}

// write 시 trim을 수행해야 하는 요구사항이 추가된다면? => 상속으로 추가될 기능이 추가된 commentService 추가
// write 시 http가 포함된 comment는 받지 않는다. 추가...
// 
fun main() {
//  val client = Client(CommentService())
//  val client = Client(TrimmingCommentService())
  val client = Client(HttpRejectCommentService())
  client.write("comment1..")
  client.write("    comment2..")
  client.write("http://localhost:8080")
}