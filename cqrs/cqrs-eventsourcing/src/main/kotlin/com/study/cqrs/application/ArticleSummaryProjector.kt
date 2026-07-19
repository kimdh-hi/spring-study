package com.study.cqrs.application

import com.study.cqrs.domain.model.ArticleEdited
import com.study.cqrs.domain.model.ArticleSummary
import com.study.cqrs.domain.model.ArticleViewed
import com.study.cqrs.domain.model.ArticleWritten
import com.study.cqrs.domain.model.CommentAdded
import com.study.cqrs.domain.repository.ArticleSummaryRepository
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class ArticleSummaryProjector(
  private val summaries: ArticleSummaryRepository,
) {
  @EventListener
  fun on(e: ArticleWritten) {
    summaries.save(ArticleSummary(e.articleId, e.title, e.authorId, e.createdAt))
  }

  @EventListener
  fun on(e: ArticleEdited) {
    summaries.findById(e.articleId).ifPresent { it.title = e.title }
  }

  @EventListener
  fun on(e: CommentAdded) {
    summaries.findById(e.articleId).ifPresent { it.commentCount++ }
  }

  @EventListener
  fun on(e: ArticleViewed) {
    summaries.findById(e.articleId).ifPresent { it.viewCount++ }
  }
}
