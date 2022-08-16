package com.toy.springmapper.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MapperConfig {

//  @Bean
  fun objectMapper() = ObjectMapper().registerKotlinModule()
}