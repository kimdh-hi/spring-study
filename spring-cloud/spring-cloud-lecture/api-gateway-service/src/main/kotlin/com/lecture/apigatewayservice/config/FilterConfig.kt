package com.lecture.apigatewayservice.config

import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FilterConfig {

  @Bean
  fun gatewayRouteLocator(builder: RouteLocatorBuilder): RouteLocator = builder.routes()
    .route {
      it.path("/first-service/**")
        .filters { filter ->
          filter
            .addRequestHeader("first-req-header-key", "first-req-header-value")
            .addRequestHeader("first-res-header-key", "first-res-header-value")
        }
        .uri("http://localhost:8881")
    }
    .route {
      it.path("/second-service/**")
        .filters { filter ->
          filter
            .addRequestHeader("second-req-header-key", "second-req-header-value")
            .addRequestHeader("second-res-header-key", "second-res-header-value")
        }
        .uri("http://localhost:8882")
    }
    .build()
}