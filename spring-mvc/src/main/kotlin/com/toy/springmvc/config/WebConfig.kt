package com.toy.springmvc.config

import com.toy.springmvc.domain.Person
import com.toy.springmvc.interceptors.AnotherSampleInterceptor
import com.toy.springmvc.interceptors.SampleInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.CacheControl
import org.springframework.mobile.device.DeviceHandlerMethodArgumentResolver
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor
import org.springframework.mobile.device.DeviceUtils
import org.springframework.oxm.jaxb.Jaxb2Marshaller
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
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
    registry.addInterceptor(deviceResolverHandlerInterceptor())
  }

  /**
   * /test-resource 이하 리소스 요청은 classpath root 의 test-resource 이하에서 찾는다.
   */
  override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
    registry.addResourceHandler("/test-resource/**")
      .addResourceLocations("classpath:/test-resource/")
      .setCacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES))
  }

  @Bean
  fun jaxb2Marshaller(): Jaxb2Marshaller {
    val marshaller = Jaxb2Marshaller()
    marshaller.setPackagesToScan(Person::class.java.`package`.name)
    return marshaller
  }

  override fun addCorsMappings(registry: CorsRegistry) {
    registry.addMapping("/**")
      .allowedOrigins("http://localhost:8888")
  }

  @Bean
  fun deviceResolverHandlerInterceptor(): DeviceResolverHandlerInterceptor {
    return DeviceResolverHandlerInterceptor()
  }

  @Bean
  fun deviceHandlerMethodArgumentResolver(): DeviceHandlerMethodArgumentResolver {
    return DeviceHandlerMethodArgumentResolver()
  }

  override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver>) {
    argumentResolvers.add(deviceHandlerMethodArgumentResolver())
  }

  @Bean
  fun deviceUtils() = DeviceUtils()
}