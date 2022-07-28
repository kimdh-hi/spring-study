package com.toy.hibernatelisteners.domain.listeners

import org.hibernate.event.spi.*
import org.hibernate.persister.entity.EntityPersister
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class EntityChangeLoggingListener: PostInsertEventListener, PostDeleteEventListener, PostUpdateEventListener {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun onPostInsert(event: PostInsertEvent) {
    log.info("onPostInsert {}", event.id)
  }

  override fun onPostDelete(event: PostDeleteEvent) {
    log.info("onPostDelete {}", event.id)
  }

  override fun onPostUpdate(event: PostUpdateEvent) {
    log.info("onPostUpdate {}", event.id)
  }

  @Deprecated("Deprecated in Java", ReplaceWith("true"))
  override fun requiresPostCommitHanding(persister: EntityPersister): Boolean = true
}