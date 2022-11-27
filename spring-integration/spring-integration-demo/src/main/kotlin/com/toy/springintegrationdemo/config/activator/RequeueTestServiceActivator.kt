package com.toy.springintegrationdemo.config.activator

import com.toy.springintegrationdemo.service.TestService
import org.slf4j.LoggerFactory
import org.springframework.amqp.AmqpRejectAndDontRequeueException
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.stereotype.Component
import java.io.Serial
import java.io.Serializable

@Component
class RequeueTestServiceActivator(
  private val testService: TestService
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @ServiceActivator
  fun execute(message: RequeueTestMessage) {
    log.info("RequeueTestServiceActivator execute message: {}", message)
    try {
      testService.logic()
    } catch (e: RuntimeException) {
      // 아래 예외가 throw 되야 requeue 가 발생하지 않고 dlx 로 메시지가 전달된다.
      throw AmqpRejectAndDontRequeueException("RequeueTestServiceActivator execute failed...")
    }
    log.info("RequeueTestServiceActivator execute success!")
  }

  @ServiceActivator
  fun requeue(message: RequeueTestMessage) {
    log.info("RequeueTestServiceActivator requeue message: {}", message)
    try {
      testService.logic()
    } catch (e: RuntimeException) {
      throw AmqpRejectAndDontRequeueException("RequeueTestServiceActivator requeue failed...")
    }
    log.info("RequeueTestServiceActivator requeue success!")
  }
}

data class RequeueTestMessage(
  var data: String = "data",
): Serializable {
  companion object {
    @Serial
    private const val serialVersionUID: Long = -8587357979390340162L
  }
}