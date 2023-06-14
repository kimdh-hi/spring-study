package com.toy.springjpatsid.config

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import javax.persistence.EntityManager

const val TSID_STRATEGY = "io.hypersistence.utils.hibernate.id.TsidGenerator"

@EnableJpaAuditing
@Configuration
class JpaConfig {

  @Bean
  fun jpaQuery(entityManager: EntityManager) = JPAQueryFactory(entityManager)
}