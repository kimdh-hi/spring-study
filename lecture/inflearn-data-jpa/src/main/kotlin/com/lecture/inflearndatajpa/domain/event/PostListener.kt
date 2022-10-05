package com.lecture.inflearndatajpa.domain.event

import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationListener
import org.springframework.context.event.EventListener

class PostListener {
  private val log = LoggerFactory.getLogger(javaClass)

  @EventListener
  fun postPublishEvent(event: PostPublishedEvent) {
    log.info("=======PostListener post:{}=======", event.post)
  }
}

//class PostListener: ApplicationListener<PostPublishedEvent> {
//  private val log = LoggerFactory.getLogger(javaClass)
//
//  override fun onApplicationEvent(event: PostPublishedEvent) {
//    log.info("=======PostListener post:{}=======", event.post)
//  }
//}