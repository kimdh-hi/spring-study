package com.toy.springcore.event

import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationListener
import org.springframework.context.event.EventListener
import org.springframework.core.annotation.Order
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component


/**
 * spring 4.2 이전 방식
 */
//@Component
//class MyEventHandler: ApplicationListener<MyEvent> {
//  private val log = LoggerFactory.getLogger(javaClass)
//
//  override fun onApplicationEvent(event: MyEvent) {
//    log.info("[MyEventHandler] onApplicationEvent: {}", event)
//  }
//}

@Component
class MyEventHandler {
  private val log = LoggerFactory.getLogger(javaClass)

  @EventListener
  @Async
  @Order(0)
  fun handle(event: MyEvent) {
    log.info("[MyEventHandler] handle: {}, Thread: {}", event, Thread.currentThread().name)
  }
}

@Component
class AnotherMyEventHandler {
  private val log = LoggerFactory.getLogger(javaClass)

  @EventListener
  @Async
  @Order(1)
  fun handle(event: MyEvent) {
    log.info("[AnotherMyEventHandler] handle: {}, Thread: {}", event, Thread.currentThread().name)
  }
}