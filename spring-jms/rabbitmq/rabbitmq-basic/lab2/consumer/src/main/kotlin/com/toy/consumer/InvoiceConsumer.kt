package com.toy.consumer

import com.toy.domain.InvoiceCreatedMessage
import com.toy.domain.InvoicePaidMessage
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

/*
한 개 큐에 여러 타입의 메시지가 있는 경우 @RabbitHandler
 */
@Service
@RabbitListener(queues = ["q.invoice"])
class InvoiceConsumer {

  private val log = LoggerFactory.getLogger(javaClass)

  @RabbitHandler
  fun handleInvoiceCreated(message: InvoiceCreatedMessage) {
    log.info("invoice-created message: {}", message)
  }

  @RabbitHandler
  fun handleInvoicePaid(message: InvoicePaidMessage) {
    log.info("invoice-paid message: {}", message)
  }
}