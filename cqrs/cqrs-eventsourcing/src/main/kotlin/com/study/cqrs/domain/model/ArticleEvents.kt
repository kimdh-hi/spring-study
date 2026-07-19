package com.study.cqrs.domain.model

import java.time.LocalDateTime

sealed interface ArticleEvent {
  val articleId: String
}

data class ArticleWritten(
  override val articleId: String,
  val title: String,
  val content: String,
  val authorId: String,
  val createdAt: LocalDateTime,
) : ArticleEvent

data class ArticleEdited(
  override val articleId: String,
  val title: String,
  val content: String,
) : ArticleEvent

data class CommentAdded(
  override val articleId: String,
  val content: String,
) : ArticleEvent

data class ArticleViewed(
  override val articleId: String,
) : ArticleEvent
