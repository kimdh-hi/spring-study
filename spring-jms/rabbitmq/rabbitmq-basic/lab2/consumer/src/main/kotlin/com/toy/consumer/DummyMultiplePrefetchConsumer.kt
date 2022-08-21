package com.toy.consumer

import com.toy.domain.DummyMessage
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

/*
prefetch 에 대한 글로벌한 설정은 yml 로 가능하지만 consumer 의 특성에 따라 prefetch 설정이 가능해야 한다.

메시지 처리 시간이 길다 => prefetch 작게
메시지 처리 시간이 짧다 => prefetch 길게 or default
 */
@Service
class DummyMultiplePrefetchConsumer(
  private val rabbitListenerContainerFactory: SimpleRabbitListenerContainerFactory
) {

  private val log = LoggerFactory.getLogger(javaClass)

  /*
  consumer 의 작업시간이 긴 경우 prefetch 가 큰 의미가 없을 것이고, 서버 인스턴스가 늘어나는 등으로 consumer 가 스케일 아웃될 때
  prefetch 로 인한 성능저하를 막도록 prefetch count 를 조정한다.
  글로벌 한 설정이 아닌 특정 consumer 에만 적용되도록 한다.
   */
  @RabbitListener(queues = ["q.long-time"], concurrency = "2", containerFactory = "prefetchOne")
  fun longTimeProcessingConsumer(dummyMessage: DummyMessage) {
    log.info("long-time message: {}", dummyMessage)
    TimeUnit.SECONDS.sleep(10)
  }

  @RabbitListener(queues = ["q.short-time"], concurrency = "2")
  fun shortTimeProcessingConsumer(dummyMessage: DummyMessage) {
    log.info("short-time message: {}", dummyMessage)
    TimeUnit.SECONDS.sleep(1)
  }
}