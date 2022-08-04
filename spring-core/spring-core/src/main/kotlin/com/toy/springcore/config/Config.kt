package com.toy.springcore.config

import com.zaxxer.hikari.HikariDataSource
import org.apache.commons.configuration2.DatabaseConfiguration
import org.apache.commons.configuration2.builder.BasicConfigurationBuilder
import org.apache.commons.configuration2.builder.fluent.Parameters
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import javax.sql.DataSource

@Configuration
class AppConfig {

  @Bean
  @ConfigurationProperties("spring.datasource.hikari")
  fun dataSource(): DataSource {
    return DataSourceBuilder.create()
      .type(HikariDataSource::class.java)
      .build()
  }

  @Bean
  fun cachedDatabaseConfiguration(dataSource: DataSource): CachedDatabaseConfiguration {
    return CachedDatabaseConfiguration.of(
      dataSource = dataSource,
      table = "zt_config", keyColumn = "id", valueColumn = "value"
    )
  }
}

open class CachedDatabaseConfiguration: DatabaseConfiguration() {

  /**
   * @Lazy
   * CachedDatabaseConfiguration 빈이 생성될 때까지 DI 받는 시점을 미룬다.
   */
  @Lazy
  @Autowired
  lateinit var dbConfig: CachedDatabaseConfiguration

  private val log = LoggerFactory.getLogger(javaClass)

  companion object {
    fun of(dataSource: DataSource, table: String, keyColumn: String, valueColumn: String): CachedDatabaseConfiguration {
      return BasicConfigurationBuilder(CachedDatabaseConfiguration::class.java)
        .configure(
          Parameters().database()
            .setDataSource(dataSource)
            .setTable(table)
            .setKeyColumn(keyColumn)
            .setValueColumn(valueColumn)
        ).configuration
    }
  }
}