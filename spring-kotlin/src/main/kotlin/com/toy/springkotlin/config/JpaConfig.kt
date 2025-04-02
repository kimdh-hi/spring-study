package com.toy.springkotlin.config

import com.querydsl.jpa.JPQLTemplates
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@Configuration
@EnableJpaAuditing
class JpaConfig {
  @Bean
  fun jpaQueryFactory(entityManager: EntityManager) = JPAQueryFactory(JPQLTemplates.DEFAULT, entityManager)
}
