package com.study.cqrs.domain.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
class ArticleSummary(
  @Id
  val articleId: String,
  var title: String,
  var authorId: String,
  val createdAt: LocalDateTime,
) {
  var commentCount: Int = 0
  var viewCount: Int = 0
}
