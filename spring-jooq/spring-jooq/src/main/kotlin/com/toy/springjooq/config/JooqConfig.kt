package com.toy.springjooq.config

import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JooqConfig {

  @Bean
  fun customizer() = DefaultConfigurationCustomizer {
    it.settings().withRenderSchema(false)
  }
}
