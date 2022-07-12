package com.toy.reactivejdsl.config

import com.toy.reactivejdsl.common.SecurityUtils
import com.toy.webfluxr2dbcpostgres.auth.JwtPrincipal
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.reactive.awaitFirstOrDefault
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.domain.ReactiveAuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import reactor.core.publisher.Mono
import java.util.*

@EnableJpaAuditing
@Configuration
class JpaConfig {

  @Bean
  fun auditorProvider(): SecurityAuditorAware {
    return SecurityAuditorAware()
  }

//  @Bean
//  fun reactiveAuditorAware(): ReactiveAuditorAware<String> {
//    return ReactiveAuditorAware<String> {
//      ReactiveSecurityContextHolder.getContext()
//        .map { it.authentication }
//        .filter { it.isAuthenticated }
//        .filter { it.principal is JwtPrincipal }
//        .map { it.principal }
//        .cast(JwtPrincipal::class.java)
//        .map { it.userId }
//    }
//  }
}

class SecurityAuditorAware: ReactiveAuditorAware<String> {

  override fun getCurrentAuditor(): Mono<String> {
    return Mono.just("aaa")
  }
}

//override fun getCurrentAuditor(): Mono<String> {
//  return ReactiveSecurityContextHolder.getContext()
//    .map(SecurityContext::getAuthentication)
//    .filter { obj: Authentication -> obj.isAuthenticated }
//    .map { obj: Authentication -> obj.principal as User }
//    .flatMap { Mono.just(it.username) }
//}