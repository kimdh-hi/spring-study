package com.toy.jpacustomgenerator.config

import com.toy.jpacustomgenerator.common.CustomIdGenerator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JpaConfig {

  @Bean
  fun customIdGenerator() = CustomIdGenerator()
}