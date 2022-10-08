package com.toy.springcore.aop

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service

//@Primary
//@Service
class ProxyEventService(
  private val eventService: EventService
): EventService {
  private val log = LoggerFactory.getLogger(javaClass)

  override fun createEvent() {
    val startTime = System.currentTimeMillis()
    eventService.createEvent()
    log.info("execute time: {}", System.currentTimeMillis() - startTime)
  }

  override fun publishEvent() {
    val startTime = System.currentTimeMillis()
    eventService.publishEvent()
    log.info("execute time: {}", System.currentTimeMillis() - startTime)
  }

  override fun deleteEvent() {
    val startTime = System.currentTimeMillis()
    eventService.deleteEvent()
    log.info("execute time: {}", System.currentTimeMillis() - startTime)
  }
}