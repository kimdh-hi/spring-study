package com.study.cqrs.domain.model

import java.time.LocalDateTime
import java.util.UUID

class Article private constructor() {
  var id: String = ""
    private set
  var title: String = ""
    private set
  var content: String = ""
    private set
  var authorId: String = ""
    private set
  var createdAt: LocalDateTime? = null
    private set
  var commentCount: Int = 0
    private set
  var viewCount: Int = 0
    private set

  private val changes = mutableListOf<ArticleEvent>()

  fun pullChanges(): List<ArticleEvent> = changes.toList().also { changes.clear() }

  fun edit(title: String, content: String) = raise(ArticleEdited(id, title, content))

  fun addComment(content: String) = raise(CommentAdded(id, content))

  fun view() = raise(ArticleViewed(id))

  private fun raise(event: ArticleEvent) {
    mutate(event)
    changes.add(event)
  }

  private fun mutate(event: ArticleEvent) {
    when (event) {
      is ArticleWritten -> {
        id = event.articleId
        title = event.title
        content = event.content
        authorId = event.authorId
        createdAt = event.createdAt
      }
      is ArticleEdited -> {
        title = event.title
        content = event.content
      }
      is CommentAdded -> commentCount++
      is ArticleViewed -> viewCount++
    }
  }

  companion object {
    fun write(title: String, content: String, authorId: String): Article =
      Article().apply {
        raise(ArticleWritten(UUID.randomUUID().toString(), title, content, authorId, LocalDateTime.now()))
      }

    fun replay(events: List<ArticleEvent>): Article =
      Article().apply { events.forEach { mutate(it) } }
  }
}
