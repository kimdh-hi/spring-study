package com.study.jpacore.config

import com.zaxxer.hikari.HikariDataSource
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy

@Configuration(proxyBeanMethods = false)
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
