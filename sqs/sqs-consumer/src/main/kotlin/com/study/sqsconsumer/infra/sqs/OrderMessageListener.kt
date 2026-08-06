package com.study.sqsconsumer.infra.sqs

import com.study.sqsconsumer.application.OrderProcessor
import com.study.sqsconsumer.domain.model.OrderMessage
import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.stereotype.Component

@Component
class OrderMessageListener(
  private val orderProcessor: OrderProcessor,
) {
  @SqsListener(SqsQueue.ORDER)
  fun onOrder(message: OrderMessage) {
    orderProcessor.process(message)
  }

  @SqsListener(SqsQueue.ORDER_FIFO)
  fun onOrderedOrder(message: OrderMessage) {
    orderProcessor.process(message)
  }
}
