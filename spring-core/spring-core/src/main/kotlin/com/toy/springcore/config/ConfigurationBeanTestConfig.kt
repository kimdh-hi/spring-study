package com.toy.springcore.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

//@Configuration(proxyBeanMethods = false)
@Configuration
class ConfigurationBeanConfig {

  @Bean
  fun someBean() = SomeBean()

  @Bean
  fun firstBean() = FirstBean(someBean())


  @Bean
  fun secondBean(someComponentBean: SomeComponentBean) = SecondBean(someComponentBean)

  @Bean
  fun thirdBean(someComponentBean: SomeComponentBean) = ThirdBean(someComponentBean)
}

class SomeBean {

  fun printSomeBean() = this
}

class FirstBean(
  private val bean: SomeBean
) {
  fun printSomeBean() = bean

}

@Component
class SomeComponentBean {

  fun print() = this
}

class SecondBean(
  private val bean: SomeComponentBean
) {
  fun print() = bean
}

class ThirdBean(
  private val bean: SomeComponentBean
) {
  fun print() = bean
}