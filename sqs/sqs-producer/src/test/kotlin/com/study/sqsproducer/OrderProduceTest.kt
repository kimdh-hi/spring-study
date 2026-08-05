package com.study.sqsproducer

import com.study.sqsproducer.domain.model.OrderMessage
import com.study.sqsproducer.domain.repository.OrderRepository
import com.study.sqsproducer.infra.sqs.SqsQueue
import io.awspring.cloud.sqs.operations.SqsTemplate
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.testcontainers.localstack.LocalStackContainer
import java.time.Duration
import kotlin.test.assertEquals

@SpringBootTest(properties = ["spring.cloud.aws.sqs.queue-not-found-strategy=create"])
@AutoConfigureMockMvc
@Import(OrderProduceTest.LocalStackTestConfig::class)
class OrderProduceTest {

  @TestConfiguration(proxyBeanMethods = false)
  class LocalStackTestConfig {
    @Bean
    @ServiceConnection
    fun localStackContainer(): LocalStackContainer =
      LocalStackContainer("localstack/localstack:4")
        .withServices("sqs")
  }

  @Autowired
  private lateinit var mockMvc: MockMvc

  @Autowired
  private lateinit var sqsTemplate: SqsTemplate

  @Autowired
  private lateinit var orderRepository: OrderRepository

  @Test
  fun `표준 큐로 주문 메세지를 발행한다`() {
    placeOrder("/orders", "keyboard", 2, "c-1")

    val received = sqsTemplate.receive(SqsQueue.ORDER.queueName, OrderMessage::class.java)

    val message = received.orElseThrow().payload
    assertEquals("keyboard", message.productName)
    assertEquals(2, message.quantity)
    assertEquals("c-1", message.customerId)
    assertEquals(message.orderId, orderRepository.findById(message.orderId).orElseThrow().id)
  }

  @Test
  fun `FIFO 큐는 같은 messageGroupId 안에서 발행 순서를 보장한다`() {
    val products = listOf("mouse", "monitor", "cable")
    products.forEach { placeOrder("/orders/fifo", it, 1, "c-fifo") }

    val received = mutableListOf<OrderMessage>()
    repeat(products.size) {
      if (received.size < products.size) {
        received += sqsTemplate.receiveMany<OrderMessage>({
          it.queue(SqsQueue.ORDER_FIFO.queueName)
            .maxNumberOfMessages(products.size - received.size)
            .pollTimeout(Duration.ofSeconds(10))
        }, OrderMessage::class.java).map { message -> message.payload }
      }
    }

    assertEquals(products, received.map { it.productName })
  }

  private fun placeOrder(path: String, productName: String, quantity: Int, customerId: String) {
    mockMvc.post(path) {
      contentType = MediaType.APPLICATION_JSON
      content = """{"productName":"$productName","quantity":$quantity,"customerId":"$customerId"}"""
    }.andExpect {
      status { isCreated() }
    }
  }
}
