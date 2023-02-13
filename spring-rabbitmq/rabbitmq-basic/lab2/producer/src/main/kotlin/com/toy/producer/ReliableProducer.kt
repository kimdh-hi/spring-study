package com.toy.producer

import com.toy.domain.DummyMessage
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.connection.CorrelationData
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class ReliableProducer(
  private val rabbitTemplate: RabbitTemplate
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @PostConstruct
  fun registerCallBack() {
    rabbitTemplate.setConfirmCallback { correlationData, ack, cause ->
      if (correlationData == null)
        return@setConfirmCallback

      if (ack)
        log.info("[valid exchange] correlation.id {}", correlationData.id)
      else {
        log.warn("[invalid exchange log1] correlation.id {}", correlationData.id)
        log.warn("[invalid exchange log2] cause {}", cause.toString())
      }
    }

    rabbitTemplate.setReturnsCallback { returned ->
      log.info("return callback")
      if (returned.replyText != null && StringUtils.equalsIgnoreCase(returned.replyText, "NO_ROUTE")) {
        log.info("replyText: {}", returned.replyText)
        val id = returned.message.messageProperties.getHeader<String>("spring_returned_message_correlation")
        log.warn("invalid routing key for message {}", id)
      }
    }
  }

  fun sendMessageWithInvalidRoutingKey(message: DummyMessage) {
    val correlationData = CorrelationData(message.publishId.toString())
    rabbitTemplate.convertAndSend("x.dummy2", "invalid-routing-key", message, correlationData)
  }

  fun sendMessageToNotExistsExchange(message: DummyMessage) {
    val correlationData = CorrelationData(message.publishId.toString())
    rabbitTemplate.convertAndSend("x.not-exists", "", message, correlationData)
  }
}