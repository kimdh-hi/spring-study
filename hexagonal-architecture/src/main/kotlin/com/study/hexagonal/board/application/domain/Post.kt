package com.study.hexagonal.board.application.domain

data class Post(
  val id: Long? = null,
  val authorId: Long,
  val title: String,
  val content: String,
  val comments: List<Comment> = emptyList()
) {
  init {
    require(title.isNotBlank()) { "제목은 필수입니다" }
    require(title.length <= 100) { "제목은 100자를 넘을 수 없습니다" }
    require(content.isNotBlank()) { "본문은 필수입니다" }
  }

  fun edit(editorId: Long, title: String, content: String): Post {
    require(editorId == authorId) { "게시글은 작성자만 수정할 수 있습니다" }
    return copy(title = title, content = content)
  }

  fun ensureDeletableBy(requesterId: Long) {
    require(requesterId == authorId) { "게시글은 작성자만 삭제할 수 있습니다" }
  }

  fun addComment(authorId: Long, content: String): Post =
    copy(comments = comments + Comment(authorId = authorId, content = content))

  fun removeComment(commentId: Long, requesterId: Long): Post {
    val comment = comments.find { it.id == commentId }
      ?: throw NoSuchElementException("댓글을 찾을 수 없습니다: $commentId")
    require(requesterId == comment.authorId || requesterId == authorId) {
      "댓글은 댓글 작성자나 게시글 작성자만 삭제할 수 있습니다"
    }
    return copy(comments = comments - comment)
  }
}
