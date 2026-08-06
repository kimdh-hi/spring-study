package com.study.sqsconsumer

import com.study.sqsconsumer.domain.model.OrderMessage
import com.study.sqsconsumer.domain.repository.ProcessedOrderRepository
import com.study.sqsconsumer.infra.sqs.SqsQueue
import io.awspring.cloud.sqs.operations.SqsTemplate
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.testcontainers.localstack.LocalStackContainer
import java.time.Duration
import java.time.Instant
import kotlin.test.assertEquals

@SpringBootTest(properties = ["spring.cloud.aws.sqs.queue-not-found-strategy=create"])
@Import(OrderConsumeTest.LocalStackTestConfig::class)
class OrderConsumeTest {

  @TestConfiguration(proxyBeanMethods = false)
  class LocalStackTestConfig {
    @Bean
    @ServiceConnection
    fun localStackContainer(): LocalStackContainer =
      LocalStackContainer("localstack/localstack:4")
        .withServices("sqs")
  }

  @Autowired
  private lateinit var sqsTemplate: SqsTemplate

  @Autowired
  private lateinit var processedOrderRepository: ProcessedOrderRepository

  @BeforeEach
  fun clear() {
    processedOrderRepository.deleteAll()
  }

  @Test
  fun `표준 큐 메세지를 소비해 주문을 저장한다`() {
    sqsTemplate.send(SqsQueue.ORDER, orderMessage(1L, "keyboard"))

    await().atMost(Duration.ofSeconds(15)).untilAsserted {
      val processed = processedOrderRepository.findById(1L).orElseThrow()
      assertEquals("keyboard", processed.productName)
      assertEquals("c-1", processed.customerId)
    }
  }

  @Test
  fun `FIFO 큐 메세지를 소비해 주문을 저장한다`() {
    sqsTemplate.send {
      it.queue(SqsQueue.ORDER_FIFO)
        .payload(orderMessage(2L, "monitor"))
        .messageGroupId("c-1")
        .messageDeduplicationId("2")
    }

    await().atMost(Duration.ofSeconds(15)).untilAsserted {
      assertEquals("monitor", processedOrderRepository.findById(2L).orElseThrow().productName)
    }
  }

  @Test
  fun `같은 주문이 중복 전달되어도 한 번만 저장한다`() {
    val message = orderMessage(3L, "cable")
    sqsTemplate.send(SqsQueue.ORDER, message)
    sqsTemplate.send(SqsQueue.ORDER, message)

    await().atMost(Duration.ofSeconds(20)).during(Duration.ofSeconds(5)).untilAsserted {
      assertEquals(1, processedOrderRepository.count())
    }
  }

  private fun orderMessage(orderId: Long, productName: String) = OrderMessage(
    orderId = orderId,
    productName = productName,
    quantity = 1,
    customerId = "c-1",
    createdAt = Instant.now(),
  )
}
