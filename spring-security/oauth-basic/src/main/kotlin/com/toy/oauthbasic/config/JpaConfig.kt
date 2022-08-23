package com.toy.oauthbasic.config

import com.zaxxer.hikari.HikariDataSource
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import javax.sql.DataSource

@Configuration
class JpaConfig {

  @Bean
  @ConfigurationProperties("spring.datasource.hikari")
  fun dataSource(): DataSource {
    return DataSourceBuilder.create()
      .type(HikariDataSource::class.java)
      .build()
  }

  @Bean
  fun jdbcTemplate(dataSource: DataSource) = JdbcTemplate(dataSource)
}