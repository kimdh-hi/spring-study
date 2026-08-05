package com.study.sqsproducer.infra.sqs

enum class SqsQueue(val queueName: String) {
  ORDER("order-queue"),
  ORDER_FIFO("order-queue.fifo"),
}
