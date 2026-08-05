package com.study.sqsproducer.infra.sqs

import com.study.sqsproducer.domain.model.Order
import com.study.sqsproducer.domain.model.OrderMessage
import com.study.sqsproducer.domain.repository.OrderEventPublisher
import io.awspring.cloud.sqs.operations.SqsTemplate
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class SqsOrderEventPublisher(
  private val sqsTemplate: SqsTemplate,
) : OrderEventPublisher {
  private val log = LoggerFactory.getLogger(javaClass)

  override fun publish(order: Order) {
    val result = sqsTemplate.send(SqsQueue.ORDER.queueName, OrderMessage.from(order))
    log.info("sent to {} messageId={}", SqsQueue.ORDER.queueName, result.messageId)
  }

  override fun publishOrdered(order: Order) {
    val result = sqsTemplate.send {
      it.queue(SqsQueue.ORDER_FIFO.queueName)
        .payload(OrderMessage.from(order))
        .messageGroupId(order.customerId)
        .messageDeduplicationId(order.id.toString())
    }
    log.info("sent to {} group={} messageId={}", SqsQueue.ORDER_FIFO.queueName, order.customerId, result.messageId)
  }
}
