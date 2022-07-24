package com.toy.springquerydsl.config

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.persistence.EntityManager

@Configuration
class JpaConfig {

  @Bean
  fun jpaQueryFactory(entityManager: EntityManager) = JPAQueryFactory(entityManager)
}