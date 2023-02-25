package com.toy.springflyway.config

import com.zaxxer.hikari.HikariDataSource
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class JpaConfig {

  @Bean
  @ConfigurationProperties("spring.datasource.hikari")
  fun dataSource(): DataSource = DataSourceBuilder.create()
    .type(HikariDataSource::class.java)
    .build()
}