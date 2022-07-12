package com.toy.reactivejdsl.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.ReactiveAuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.security.core.context.ReactiveSecurityContextHolder

@EnableJpaAuditing
@Configuration
class JpaConfig {

  @Bean
  fun reactiveAuditorAware(): ReactiveAuditorAware<String> {
    return ReactiveAuditorAware<String> {
      ReactiveSecurityContextHolder.getContext()
        .map { it.authentication }
        .filter { it.isAuthenticated }
        .map { it.name }
    }
  }
}