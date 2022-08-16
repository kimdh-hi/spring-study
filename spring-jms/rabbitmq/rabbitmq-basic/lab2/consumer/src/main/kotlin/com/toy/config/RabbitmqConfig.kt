package com.toy.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.json.JsonMapper
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitmqConfig {

  @Bean
  fun objectMapper() = JsonMapper
    .builder()
    .findAndAddModules()
    .build()

  @Bean
  fun converter(@Autowired objectMapper: ObjectMapper) = Jackson2JsonMessageConverter(objectMapper)
}