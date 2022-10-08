package com.toy.springcore.aop

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class EventServiceImpl: EventService {
  private val log = LoggerFactory.getLogger(javaClass)

  @PerfLog
  override fun createEvent() {
//    val startTime = System.currentTimeMillis()
    Thread.sleep(1000)
    log.info("createEvent")
//    log.info("execute time: {}", System.currentTimeMillis() - startTime)
  }

  @PerfLog
  override fun publishEvent() {
//    val startTime = System.currentTimeMillis()
    Thread.sleep(2000)
    log.info("publishEvent")
//    log.info("execute time: {}", System.currentTimeMillis() - startTime)
  }

  @PerfLog
  override fun deleteEvent() {
    Thread.sleep(1000)
    log.info("deleteEvent")
  }
}