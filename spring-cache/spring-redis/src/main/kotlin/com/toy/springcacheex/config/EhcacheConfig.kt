package com.toy.springcacheex.config

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.ehcache.EhCacheCacheManager
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource

@Configuration
@ConditionalOnProperty(value = ["spring.cache.type"], havingValue = "EHCACHE")
@EnableCaching
class EhcacheConfig {

  private val log = LoggerFactory.getLogger(javaClass)

  @Bean
  fun cacheManager(): EhCacheCacheManager {
    log.info("ehcache cacheManager bean register")
    return EhCacheCacheManager(ehCacheManager().`object`!!)
  }

  @Bean
  fun ehCacheManager(): EhCacheManagerFactoryBean {
    val bean = EhCacheManagerFactoryBean()
    bean.setConfigLocation(ClassPathResource("ehcache.xml"))
    bean.setShared(true)
    return bean
  }
}