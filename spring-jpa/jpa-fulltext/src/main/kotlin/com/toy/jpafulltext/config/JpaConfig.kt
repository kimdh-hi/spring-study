package com.toy.jpafulltext.config

import com.querydsl.jpa.impl.JPAQueryFactory
import com.zaxxer.hikari.HikariDataSource
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.persistence.EntityManager
import javax.sql.DataSource

@Configuration
class JpaConfig {

  @Bean
  fun jpaQueryFactory(entityManager: EntityManager): JPAQueryFactory {
    return JPAQueryFactory(entityManager)
  }

  @Bean
  @ConfigurationProperties("spring.datasource.hikari")
  fun masterDataSource(): DataSource = DataSourceBuilder.create()
    .type(HikariDataSource::class.java)
    .build()
}