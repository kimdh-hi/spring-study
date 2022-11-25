package com.toy.springintegrationdemo.config.activator

import org.slf4j.LoggerFactory
import org.springframework.amqp.AmqpRejectAndDontRequeueException
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.stereotype.Component
import java.io.Serial
import java.io.Serializable

@Component
class RequeueTestServiceActivator {

  private val log = LoggerFactory.getLogger(javaClass)

  @ServiceActivator
  fun execute(message: RequeueTestMessage) {
    log.info("RequeueTestServiceActivator receive message: {}", message)
    message.data ?: run {
      log.warn("message cannot be null...")
      // 아래 예외가 throw 되야 requeue 가 발생하지 않고 dlx 로 메시지가 전달된다.
      throw AmqpRejectAndDontRequeueException("message cannot be null...")
//      throw RuntimeException("message cannot be null...")
    }
    log.info("RequeueTestServiceActivator success!")
  }
}

data class RequeueTestMessage(
  val data: String? = null,
): Serializable {
  companion object {
    @Serial
    private const val serialVersionUID: Long = -8587357979390340162L
  }
}