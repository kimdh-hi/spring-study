package com.study.cqrs.presentation.query

import com.study.cqrs.application.query.ArticleQueryService
import com.study.cqrs.domain.model.ArticleSummary
import com.study.cqrs.domain.model.StoredEvent
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
class ArticleQueryController(
  private val service: ArticleQueryService,
) {
  @GetMapping("/articles")
  fun list(): List<ArticleSummaryResponse> =
    service.list().map { it.toResponse() }

  @GetMapping("/articles/{id}")
  fun get(@PathVariable id: String): ArticleSummaryResponse =
    service.get(id).toResponse()

  @GetMapping("/articles/{id}/events")
  fun events(@PathVariable id: String): List<StoredEventResponse> =
    service.history(id).map { it.toResponse() }
}

data class ArticleSummaryResponse(
  val articleId: String,
  val title: String,
  val authorId: String,
  val commentCount: Int,
  val viewCount: Int,
  val createdAt: LocalDateTime,
)

data class StoredEventResponse(
  val sequence: Int,
  val type: String,
  val payload: String,
  val occurredAt: LocalDateTime,
)

private fun ArticleSummary.toResponse() =
  ArticleSummaryResponse(articleId, title, authorId, commentCount, viewCount, createdAt)

private fun StoredEvent.toResponse() =
  StoredEventResponse(sequence, type, payload, occurredAt)
