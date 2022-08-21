package com.toy.consumer

import com.toy.domain.DummyMessage
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

/*
prefetch?

springboot 의 기본설정을 사용하고 있다면 consumer 는 메시지를 받을 때마다 mq를 통하지 않는다.
consume 대상 메시지를 consumer 측의 메모리에 올려놓는다.
모든 메시지에 대해 매번 mq 를 통하지 않는 것은 성능상 효과적이겠지만 특정 상황에서 성능 저하 포인트가 될 수 있다.

prefetch default: 250

n 개 consumer 가 concurrency 하게 메시지를 받아 처리하고 있다고 가정.
prefetch 에 의해 n개 메시지를 모두 consumer 측의 메모리에 적재 후 작업이 이루어 짐
prefetch 에 의해 모든 메시지가 이미 메모리에 올라가버렸다면 이후 consumer 를 추가한다고 해도 추가 된 consumer 는 메시지를 받아 처리할 수 없다.

why?
consumer 는 ready 상태의 메시지를 받아오는데 prefetch 에 의해 모두 ready 상태의 메시지를 메모리에 올렸기 때문.
consumer 를 추가해도 예상한 효과를 볼 수 없는 것

prefetch 값을 조정해서 새로운 consumer 를 추가하는 상황에도 예상한 만큼의 성능이 나오도록 해보자.

global 설정 (yml)
spring.batch.listener.simple.prefetch=1 (default: 250)
 */
@Service
class DummyPrefetchConsumer {

  private val log = LoggerFactory.getLogger(javaClass)

  @RabbitListener(queues = ["q.dummy"], concurrency = "2")
  fun listener(dummyMessage: DummyMessage) {
    log.info("dummyMessage: {}", dummyMessage)
    TimeUnit.SECONDS.sleep(20)
  }
}