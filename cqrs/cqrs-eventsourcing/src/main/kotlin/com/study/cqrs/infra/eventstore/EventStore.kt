package com.study.cqrs.infra.eventstore

import com.study.cqrs.domain.model.ArticleEdited
import com.study.cqrs.domain.model.ArticleEvent
import com.study.cqrs.domain.model.ArticleViewed
import com.study.cqrs.domain.model.ArticleWritten
import com.study.cqrs.domain.model.CommentAdded
import com.study.cqrs.domain.model.StoredEvent
import com.study.cqrs.domain.repository.StoredEventRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import tools.jackson.databind.ObjectMapper
import java.time.LocalDateTime

@Component
class EventStore(
  private val repository: StoredEventRepository,
  private val mapper: ObjectMapper,
  private val publisher: ApplicationEventPublisher,
) {
  fun load(aggregateId: String): List<ArticleEvent> =
    repository.findByAggregateIdOrderBySequenceAsc(aggregateId).map { deserialize(it.type, it.payload) }

  fun append(events: List<ArticleEvent>) {
    if (events.isEmpty()) return
    var sequence = repository.countByAggregateId(events.first().articleId).toInt()
    events.forEach { event ->
      repository.save(
        StoredEvent(
          event.articleId,
          sequence++,
          event::class.simpleName!!,
          mapper.writeValueAsString(event),
          LocalDateTime.now(),
        ),
      )
      publisher.publishEvent(event)
    }
  }

  private fun deserialize(type: String, payload: String): ArticleEvent =
    when (type) {
      "ArticleWritten" -> mapper.readValue(payload, ArticleWritten::class.java)
      "ArticleEdited" -> mapper.readValue(payload, ArticleEdited::class.java)
      "CommentAdded" -> mapper.readValue(payload, CommentAdded::class.java)
      "ArticleViewed" -> mapper.readValue(payload, ArticleViewed::class.java)
      else -> throw IllegalArgumentException("unknown event type: $type")
    }
}
