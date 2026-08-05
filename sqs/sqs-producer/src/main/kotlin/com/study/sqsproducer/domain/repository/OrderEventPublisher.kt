package com.study.sqsproducer.domain.repository

import com.study.sqsproducer.domain.model.Order

interface OrderEventPublisher {
  fun publish(order: Order)

  fun publishOrdered(order: Order)
}
