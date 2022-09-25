package com.lecture.userservice.config

import io.r2dbc.spi.ConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator

@Configuration
class R2dbcConfig {

  @Bean
  fun init(connectionFactory: ConnectionFactory): ConnectionFactoryInitializer {
    return ConnectionFactoryInitializer().apply {
      setConnectionFactory(connectionFactory)
      setDatabasePopulator(ResourceDatabasePopulator(ClassPathResource("schema.sql")))
    }
  }
}