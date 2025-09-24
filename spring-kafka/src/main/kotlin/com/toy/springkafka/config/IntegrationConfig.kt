package com.toy.springkafka.config

import com.toy.springkafka.config.handler.TestHandler
import com.toy.springkafka.config.handler.TestTopicMessage
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.header.internals.RecordHeaders
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.dsl.IntegrationFlow
import org.springframework.integration.dsl.Transformers
import org.springframework.integration.dsl.integrationFlow
import org.springframework.integration.kafka.dsl.Kafka
import org.springframework.integration.kafka.dsl.KafkaMessageDrivenChannelAdapterSpec
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.KafkaOperations
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer
import org.springframework.kafka.listener.DefaultErrorHandler
import org.springframework.util.backoff.FixedBackOff

@Configuration
class IntegrationConfig(
  private val containerFactory: ConcurrentKafkaListenerContainerFactory<Any, Any>,
  private val kafkaTemplate: KafkaOperations<Any, Any>,
  private val testHandler: TestHandler,
) {

  @Bean
  fun testFlow(): IntegrationFlow = integrationFlow(kafkaAdaptor(KafkaTopic.TEST)) {
    transform(Transformers.fromJson(TestTopicMessage::class.java))
    handle(testHandler, "handle")
  }

  private fun kafkaAdaptor(kafkaTopic: KafkaTopic): KafkaMessageDrivenChannelAdapterSpec<in Any, in Any, *> {
    val container = containerFactory.createContainer(kafkaTopic.topicName).apply {
      if (kafkaTopic.useDlt) {
        commonErrorHandler = createDltErrorHandler()
      }
    }

    return Kafka.messageDrivenChannelAdapter(container)
  }

  private fun createDltErrorHandler(): DefaultErrorHandler {
    val recoverer = DeadLetterPublishingRecoverer(kafkaTemplate) { record, _ ->
      TopicPartition("${record.topic()}.dlt", -1)
    }.apply {
      listOf(
        DeadLetterPublishingRecoverer.HeaderNames.HeadersToAdd.EX_STACKTRACE,
        DeadLetterPublishingRecoverer.HeaderNames.HeadersToAdd.TS_TYPE,
        DeadLetterPublishingRecoverer.HeaderNames.HeadersToAdd.EX_CAUSE,
        DeadLetterPublishingRecoverer.HeaderNames.HeadersToAdd.EXCEPTION,
        DeadLetterPublishingRecoverer.HeaderNames.HeadersToAdd.EX_MSG,
        DeadLetterPublishingRecoverer.HeaderNames.HeadersToAdd.OFFSET,
        DeadLetterPublishingRecoverer.HeaderNames.HeadersToAdd.TS,
        DeadLetterPublishingRecoverer.HeaderNames.HeadersToAdd.PARTITION
      ).forEach { excludeHeader(it) }

      setHeadersFunction { _, ex -> setCustomHeader(ex) }
    }

    val backOff = FixedBackOff(0, 2)
    return DefaultErrorHandler(recoverer, backOff)
  }

  private fun setCustomHeader(ex: Throwable): RecordHeaders {
    val rootCause = ex.getRootCause()
    return RecordHeaders().apply {
      add("error-class", rootCause.javaClass.simpleName.toByteArray())
      rootCause.message?.let { add("error-message", it.toByteArray()) }
    }
  }

  private fun Throwable.getRootCause(): Throwable {
    var cause = this
    while (cause.cause != null && cause.cause !== cause) {
      cause = cause.cause!!
    }
    return cause
  }
}
