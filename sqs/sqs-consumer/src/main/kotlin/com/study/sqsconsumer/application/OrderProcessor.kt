package com.study.sqsconsumer.application

import com.study.sqsconsumer.domain.model.OrderMessage
import com.study.sqsconsumer.domain.model.ProcessedOrder
import com.study.sqsconsumer.domain.repository.ProcessedOrderRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderProcessor(
  private val processedOrderRepository: ProcessedOrderRepository,
) {
  private val log = LoggerFactory.getLogger(javaClass)

  @Transactional
  fun process(message: OrderMessage) {
    if (processedOrderRepository.existsById(message.orderId)) {
      log.info("duplicate skipped orderId={}", message.orderId)
      return
    }
    processedOrderRepository.save(ProcessedOrder.from(message))
    log.info("processed orderId={} product={} quantity={}", message.orderId, message.productName, message.quantity)
  }
}
