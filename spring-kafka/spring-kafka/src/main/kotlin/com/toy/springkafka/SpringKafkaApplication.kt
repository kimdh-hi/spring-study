package com.toy.springkafka

import org.apache.kafka.clients.admin.AdminClient
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.kafka.core.KafkaTemplate
import java.util.Collections

@SpringBootApplication
class SpringKafkaApplication {

  @Bean
  fun runner(
    kafkaTemplate: KafkaTemplate<String, String>,
    adminClient: AdminClient
  ) = ApplicationRunner {

    kafkaTemplate.send("test-topic", "test")

    val topics = adminClient.listTopics().namesToListings().get()
    topics.keys.forEach { topicName ->
      val topic = topics[topicName]
      println(topic)
      val description = adminClient.describeTopics(listOf(topicName)).allTopicNames().get()
      println(description)

      if(topic != null && topic.isInternal)
        adminClient.deleteTopics(listOf(topicName))
    }
  }
}

fun main(args: Array<String>) {
  runApplication<SpringKafkaApplication>(*args)
}
