package com.toy.springkafka.config

import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.common.config.TopicConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.KafkaAdmin

@Configuration
class KafkaConfig {

  @Bean
  fun newTopic1(): NewTopic = TopicBuilder.name("newTopic1").build()

  @Bean
  fun newTopics(): KafkaAdmin.NewTopics = KafkaAdmin.NewTopics(
    TopicBuilder.name("newTopics1").build(),
    TopicBuilder.name("newTopics2")
      .partitions(3)
      .replicas(1)
      .config(TopicConfig.RETENTION_MS_CONFIG, (1000 * 60 * 60).toString())
      .build()
  )

  @Bean
  fun adminClient(kafkaAdmin: KafkaAdmin): AdminClient = AdminClient.create(kafkaAdmin.configurationProperties)
}