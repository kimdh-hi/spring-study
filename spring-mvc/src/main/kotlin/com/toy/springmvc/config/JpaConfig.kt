package com.toy.springmvc.config

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JpaConfig {

  @Bean
  fun hibernate5Module() = Hibernate5Module()
}