package com.toy.producer

import com.toy.domain.InvoiceCanceledMessage
import com.toy.domain.InvoiceCreatedMessage
import com.toy.domain.InvoicePaidMessage
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service


/*
한 개 큐에 두 개 서로 다른 타입의 메시지를 전달
 */

@Service
class InvoiceProducer(
  private val rabbitTemplate: RabbitTemplate
) {

  companion object {
    const val EXCHANGE = "x.invoice"
  }

  fun sendInvoiceCreated(message: InvoiceCreatedMessage) {
    rabbitTemplate.convertAndSend(EXCHANGE, "", message)
  }

  fun sendInvoicePaid(message: InvoicePaidMessage) {
    rabbitTemplate.convertAndSend(EXCHANGE, "", message)
  }

  fun sendInvoiceCanceled(message: InvoiceCanceledMessage) {
    rabbitTemplate.convertAndSend(EXCHANGE, "", message)
  }
}