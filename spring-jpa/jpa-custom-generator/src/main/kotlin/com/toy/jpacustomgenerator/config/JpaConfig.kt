package com.toy.jpacustomgenerator.config

import com.querydsl.jpa.impl.JPAQueryFactory
import com.toy.jpacustomgenerator.common.CustomIdGenerator
import jakarta.persistence.EntityManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JpaConfig {

  @Bean
  fun customIdGenerator() = CustomIdGenerator()

  @Bean
  fun jpaQueryFactory(em: EntityManager) = JPAQueryFactory(em)
}