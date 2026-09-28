package com.study.jpacore.config

import com.zaxxer.hikari.HikariDataSource
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy

/**
 * Manual LazyConnectionDataSourceProxy setup used before Spring Boot 4.1.
 *
 * Spring Boot 4.1+ supports `spring.datasource.connection-fetch: lazy` instead.
 * Enabling this config makes DataSourceAutoConfiguration back off, so the Boot property has no effect.
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = ["custom.use-legacy-lazy-datasource"], havingValue = "true")
class LazyDataSourceConfig {

  @Bean
  @ConfigurationProperties(prefix = "spring.datasource.hikari")
  fun hikariDataSource(dataSourceProperties: DataSourceProperties): HikariDataSource {
    return dataSourceProperties.initializeDataSourceBuilder()
      .type(HikariDataSource::class.java)
      .build()
  }

  @Bean
  @Primary
  fun lazyConnectionDataSourceProxy(hikariDataSource: HikariDataSource): LazyConnectionDataSourceProxy {
    return LazyConnectionDataSourceProxy(hikariDataSource)
  }
}
