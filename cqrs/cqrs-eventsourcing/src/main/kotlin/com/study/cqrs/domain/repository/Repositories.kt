package com.study.cqrs.domain.repository

import com.study.cqrs.domain.model.ArticleSummary
import com.study.cqrs.domain.model.StoredEvent
import org.springframework.data.jpa.repository.JpaRepository

interface StoredEventRepository : JpaRepository<StoredEvent, Long> {
  fun findByAggregateIdOrderBySequenceAsc(aggregateId: String): List<StoredEvent>
  fun countByAggregateId(aggregateId: String): Long
}

interface ArticleSummaryRepository : JpaRepository<ArticleSummary, String>
