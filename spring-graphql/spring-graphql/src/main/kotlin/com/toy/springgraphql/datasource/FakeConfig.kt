package com.toy.springgraphql.datasource

import com.github.javafaker.Faker
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FakeConfig {

  @Bean
  fun faker() = Faker()
}