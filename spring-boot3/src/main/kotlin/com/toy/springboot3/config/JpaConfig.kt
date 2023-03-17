package com.toy.springboot3.config

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JpaConfig {

  @Bean
  fun jpaQueryFactory(entityManager: EntityManager) = JPAQueryFactory(entityManager)
}