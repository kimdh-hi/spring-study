package com.study.openfeignquerydsl.config

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class QueryDslConfig {

  @Bean
  fun jpaQueryFactory(entityManager: EntityManager) = JPAQueryFactory(entityManager)
}
