package com.toy.jpaeventlistener.listener

import com.toy.jpaeventlistener.domain.BSpace
import org.hibernate.event.spi.PostDeleteEvent
import org.hibernate.event.spi.PostDeleteEventListener
import org.hibernate.persister.entity.EntityPersister
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class BSpaceJpaListener: PostDeleteEventListener {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun onPostDelete(event: PostDeleteEvent) {
    log.info("BSpaceJpaListener called...")
    val entity = event.entity
    if (entity is BSpace) {
      event.session.actionQueue.registerProcess { success, session ->
        if (success) {
          log.info("BSpaceJpaListener.onPostDelete...{}", entity)
        }
      }
    }
  }

  override fun requiresPostCommitHandling(persister: EntityPersister?): Boolean = true
}