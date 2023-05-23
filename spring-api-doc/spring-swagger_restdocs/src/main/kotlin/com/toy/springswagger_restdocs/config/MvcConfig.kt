package com.toy.springswagger_restdocs.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class MvcConfig: WebMvcConfigurer {

  override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
    registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/")
    registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/static/swagger-ui/")
  }
}