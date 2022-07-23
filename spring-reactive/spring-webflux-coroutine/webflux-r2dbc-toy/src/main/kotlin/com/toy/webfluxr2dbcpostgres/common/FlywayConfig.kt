package com.toy.webfluxr2dbcpostgres.common

import org.flywaydb.core.Flyway
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FlywayConfig(
  private val flywayProperties: FlywayProperties
) {
  @Bean(initMethod = "migrate")
  fun flyway(): Flyway {
    val config = Flyway
      .configure()
      .dataSource(
        flywayProperties.url,
        flywayProperties.username,
        flywayProperties.password
      )
      .baselineOnMigrate(true)
    return Flyway(config)
  }
}

@ConstructorBinding
@ConfigurationProperties(prefix = "flyway")
data class FlywayProperties(
  var url: String,
  var username: String,
  var password: String
)