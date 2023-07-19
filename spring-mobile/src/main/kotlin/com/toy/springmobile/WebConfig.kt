package com.toy.springmobile

import org.springframework.context.annotation.Configuration
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor

import org.springframework.web.servlet.config.annotation.InterceptorRegistry

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
class WebConfig : WebMvcConfigurer {
  override fun addInterceptors(registry: InterceptorRegistry) {
    registry.addInterceptor(DeviceResolverHandlerInterceptor())
  }
}