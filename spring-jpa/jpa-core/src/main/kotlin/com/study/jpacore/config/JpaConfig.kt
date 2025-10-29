package com.study.jpacore.config

import com.study.jpacore.common.QueryCountInspector
import org.hibernate.cfg.AvailableSettings
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing


@EnableJpaAuditing
@Configuration
class JpaConfig {

  @Bean
  @ConditionalOnBean(QueryCountInspector::class)
  fun hibernatePropertyConfig(queryCountInspector: QueryCountInspector) =
    HibernatePropertiesCustomizer {
      it[AvailableSettings.STATEMENT_INSPECTOR] = queryCountInspector
    }
}
