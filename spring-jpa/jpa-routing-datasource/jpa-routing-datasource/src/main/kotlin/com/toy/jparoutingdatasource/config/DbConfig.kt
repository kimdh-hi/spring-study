package com.toy.jparoutingdatasource.config

import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.transaction.support.TransactionSynchronizationManager
import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
@EnableAutoConfiguration(exclude = [DataSourceAutoConfiguration::class])
class DbConfig {

  @Bean(name = ["masterDataSource"])
  @ConfigurationProperties(prefix = "spring.datasource.master.hikari")
  fun masterDataSource(): HikariDataSource = DataSourceBuilder.create()
    .type(HikariDataSource::class.java)
    .build()

  @Bean(name = ["slaveDataSource"])
  @ConfigurationProperties(prefix = "spring.datasource.slave.hikari")
  fun slaveDataSource(): HikariDataSource = DataSourceBuilder.create()
    .type(HikariDataSource::class.java)
    .build()
    .apply { isReadOnly = true }

  @Bean
  @ConditionalOnBean(name = ["masterDataSource", "slaveDataSource"])
  fun routingDataSource(
    @Qualifier("masterDataSource") masterDataSource: DataSource,
    @Qualifier("slaveDataSource") slaveDataSource: DataSource
  ): DataSource {
    val routingDataSource = RoutingDataSource()
    val dataSources: Map<Any, Any> = mapOf(
      "master" to masterDataSource,
      "slave" to slaveDataSource
    )
    routingDataSource.setTargetDataSources(dataSources)
    routingDataSource.setDefaultTargetDataSource(masterDataSource)
    return routingDataSource
  }

  @Primary
  @Bean(name = ["currentDataSource"])
  @ConditionalOnBean(name = ["routingDataSource"])
  fun currentDataSource(routingDataSource: DataSource) = LazyConnectionDataSourceProxy(routingDataSource)
}

class RoutingDataSource: AbstractRoutingDataSource() {
  override fun determineCurrentLookupKey() = when {
    TransactionSynchronizationManager.isCurrentTransactionReadOnly() -> "slave"
    else -> "master"
  }
}
