package com.study.cqrs.application.query

import com.study.cqrs.domain.model.ArticleSummary
import com.study.cqrs.domain.model.StoredEvent
import com.study.cqrs.domain.repository.ArticleSummaryRepository
import com.study.cqrs.domain.repository.StoredEventRepository
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ArticleQueryService(
  private val summaries: ArticleSummaryRepository,
  private val storedEvents: StoredEventRepository,
) {
  fun list(): List<ArticleSummary> =
    summaries.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))

  fun get(articleId: String): ArticleSummary =
    summaries.findById(articleId).orElseThrow()

  fun history(articleId: String): List<StoredEvent> =
    storedEvents.findByAggregateIdOrderBySequenceAsc(articleId)
}
