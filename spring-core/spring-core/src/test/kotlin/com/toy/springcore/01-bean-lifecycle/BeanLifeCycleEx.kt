package com.toy.springcore.`01-bean-lifecycle`

import org.junit.jupiter.api.Test
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/*
객체생성 -> 의존관계 주입
- 객체생성(빈 생성) 후 의존관계 주입이 완료되고 후처리를 하려면 그 시점에서 콜백을 받아야 한다.
- 의존관계 주입이 완료된 시점에서 콜백: InitializingBean.afterPropertiesSet
- 빈이 소멸되기 직전에서 콜백: DisposableBean
 */
class BeanLifeCycleEx {

  @Test
  fun `networkClient1 - 인터페이스 방식`() {
    val context = AnnotationConfigApplicationContext(BeanConfig::class.java)
    val client = context.getBean(NetworkClient1::class.java)
    context.close()
  }

  @Test
  fun `networkClient2 - initeMode, destroyMode`() {
    val context = AnnotationConfigApplicationContext(BeanConfig::class.java)
    val client = context.getBean(NetworkClient2::class.java)
    context.close()
  }

  @Test
  fun `networkClient3 (권장) - @PostConstruct, @PreDestroy`() {
    val context = AnnotationConfigApplicationContext(BeanConfig::class.java)
    val client = context.getBean(NetworkClient3::class.java)
    context.close()
  }

  @Configuration
  class BeanConfig {

    @Bean
    fun networkClient1(): NetworkClient1 {
      val client = NetworkClient1()
      client.url = "localhost:8000"
      return client
    }

    @Bean(initMethod = "init", destroyMethod = "close")
    fun networkClient2(): NetworkClient2 {
      val client = NetworkClient2()
      client.url = "localhost:8000"
      return client
    }

    @Bean
    fun networkClient3(): NetworkClient3 {
      val client = NetworkClient3()
      client.url = "localhost:8000"
      return client
    }
  }
}