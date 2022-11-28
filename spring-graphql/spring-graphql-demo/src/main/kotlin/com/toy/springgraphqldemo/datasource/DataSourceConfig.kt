package com.toy.springgraphqldemo.datasource

import com.github.javafaker.Faker
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DataSourceConfig {

  @Bean
  fun faker() = Faker()
}