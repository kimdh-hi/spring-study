package com.toy.springmvc.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {

  @Bean
  fun webClient(): WebClient {
    val customMapper = ObjectMapper()
    customMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    customMapper.propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE

    val exchangeStrategies = ExchangeStrategies.builder()
      .codecs {
        it.defaultCodecs().jackson2JsonEncoder(Jackson2JsonEncoder(customMapper))
        it.defaultCodecs().jackson2JsonDecoder(Jackson2JsonDecoder(customMapper))
      }
      .build()

    return WebClient.builder().build().mutate()
      .exchangeStrategies(exchangeStrategies)
      .build()
  }
}