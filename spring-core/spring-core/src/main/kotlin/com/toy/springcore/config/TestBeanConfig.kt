package com.toy.springcore.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

@Configuration
class TestBeanConfig {

  @Bean
  fun testBean() = TestBean(data = "default")

  @Bean("customBean1")
  fun customBean1() = TestBean(data = "custom1")

  @Bean("customBean2")
  fun customBean2() = TestBean(data = "custom2")
}

@Component
class TestBeanService(
  private val testBean: TestBean,
  @Qualifier("customBean1") private val testBean1: TestBean,
  @Qualifier("customBean2") private val testBean2: TestBean,
) {
  fun run() {
    println("testBean: $testBean")
    println("testBean1: $testBean1")
    println("testBean2: $testBean2")
  }
}

data class TestBean(
  val data: String
)