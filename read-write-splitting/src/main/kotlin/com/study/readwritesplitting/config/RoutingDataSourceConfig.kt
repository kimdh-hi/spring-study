package com.study.readwritesplitting.config

import com.zaxxer.hikari.HikariDataSource
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
import javax.sql.DataSource

@Configuration
@Profile("routing")
class RoutingDataSourceConfig {

  @Bean
  @ConfigurationProperties("datasource.writer")
  fun writerDataSource(): DataSource =
    DataSourceBuilder.create().type(HikariDataSource::class.java).build()

  @Bean
  @ConfigurationProperties("datasource.reader")
  fun readerDataSource(): DataSource =
    DataSourceBuilder.create().type(HikariDataSource::class.java).build()

  @Bean
  fun routingDataSource(): DataSource {
    val writer = writerDataSource()
    val reader = readerDataSource()
    return ReplicationRoutingDataSource().apply {
      setTargetDataSources(
        mapOf(
          RoutingDataSourceType.WRITE to writer,
          RoutingDataSourceType.READ to reader,
        ),
      )
      setDefaultTargetDataSource(writer)
    }
  }

  @Bean
  @Primary
  fun dataSource(): DataSource = LazyConnectionDataSourceProxy(routingDataSource())
}
