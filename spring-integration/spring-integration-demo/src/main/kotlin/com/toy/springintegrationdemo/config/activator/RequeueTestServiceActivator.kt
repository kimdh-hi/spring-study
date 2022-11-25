package com.toy.springintegrationdemo.config.activator

import org.slf4j.LoggerFactory
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
      throw RuntimeException("message cannot be null...")
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