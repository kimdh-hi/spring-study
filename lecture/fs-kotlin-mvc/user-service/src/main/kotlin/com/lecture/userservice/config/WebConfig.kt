package com.lecture.userservice.config

import com.lecture.userservice.config.annotation.AuthTokenResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.CorsRegistry
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer

@Configuration
class WebConfig: WebFluxConfigurer {

  override fun addCorsMappings(registry: CorsRegistry) {
    registry.addMapping("/**")
      .allowedOrigins("*")
      .allowedMethods("GET", "POST", "PUT", "DELETE")
      .maxAge(3600)
  }

  override fun configureArgumentResolvers(configurer: ArgumentResolverConfigurer) {
    super.configureArgumentResolvers(configurer)
    configurer.addCustomResolver(AuthTokenResolver())
  }
}