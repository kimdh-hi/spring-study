package com.toy.springbatchplus

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.support.JdbcTransactionManager
import javax.sql.DataSource

@Configuration
class JpaConfig {

  @Bean
  fun batchTransactionManager(dataSource: DataSource): JdbcTransactionManager {
    return JdbcTransactionManager(dataSource)
  }

}