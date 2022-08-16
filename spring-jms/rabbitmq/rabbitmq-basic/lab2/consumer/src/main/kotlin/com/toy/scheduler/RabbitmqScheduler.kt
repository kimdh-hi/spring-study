package com.toy.scheduler

import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
@EnableScheduling
class RabbitmqScheduler(
  private val registry: RabbitListenerEndpointRegistry
) {

  private val log = LoggerFactory.getLogger(javaClass)

  /*
  모든 Consumer 를 스케줄링을 통해 on / off
   */
  @Scheduled(cron = "0 55 21 * * *")
  fun stopAll() {
    registry.listenerContainers.forEach {
      log.info("stop rabbitmq container: {}", it)
      it.stop()
    }
  }

  @Scheduled(cron = "0 57 21 * * *")
  fun startAll() {
    registry.listenerContainers.forEach {
      log.info("start rabbitmq container: {}", it)
      it.start()
    }
  }

}