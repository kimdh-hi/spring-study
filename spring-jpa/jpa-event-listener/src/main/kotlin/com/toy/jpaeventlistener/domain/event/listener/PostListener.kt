package com.toy.jpaeventlistener.domain.event.listener

import com.toy.jpaeventlistener.domain.event.PostPublishedEvent
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component

@Component
class PostListener: ApplicationListener<PostPublishedEvent> {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun onApplicationEvent(event: PostPublishedEvent) {
    log.info("===PostListener===")
    log.info("$event")
    log.info("==================")
  }
}