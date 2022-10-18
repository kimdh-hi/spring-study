package com.toy.springmvc.config

import com.toy.springmvc.interceptors.AnotherSampleInterceptor
import com.toy.springmvc.interceptors.SampleInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.http.CacheControl
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.util.concurrent.TimeUnit

@Configuration
class WebConfig: WebMvcConfigurer {

  // formatter 빈으로 등록시 추가 설정 필요없음
//  override fun addFormatters(registry: FormatterRegistry) {
//    registry.addFormatter(PersonFormatter())
//  }

  /*
  1. anotherInterceptor.preHandler -> sampleInterceptor.preHandler
  2. sampleInterceptor.postHandler -> anotherInterceptor.postHandler
  3. sampleInterceptor.afterCompletion -> anotherInterceptor.afterCompletion
   */
  override fun addInterceptors(registry: InterceptorRegistry) {
    registry.addInterceptor(SampleInterceptor())
      .addPathPatterns("/sample/**")
      .order(0)
    registry.addInterceptor(AnotherSampleInterceptor())
      .addPathPatterns("/sample/**")
      .order(-1)
  }

  /**
   * /test-resource 이하 리소스 요청은 classpath root 의 test-resource 이하에서 찾는다.
   */
  override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
    registry.addResourceHandler("/test-resource/**")
      .addResourceLocations("classpath:/test-resource/")
      .setCacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES))
  }
}