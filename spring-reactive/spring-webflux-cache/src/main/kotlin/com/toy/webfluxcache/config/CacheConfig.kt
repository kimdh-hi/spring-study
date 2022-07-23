package com.toy.webfluxcache.config

import com.google.common.cache.CacheBuilder
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CachingConfigurer
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.concurrent.ConcurrentMapCache
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.cache.interceptor.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.lang.NonNull
import java.util.*
import java.util.concurrent.TimeUnit

@Configuration
@EnableCaching
class CacheConfig: CachingConfigurer {

  @Bean
  override fun cacheManager(): CacheManager? {
    return object : ConcurrentMapCacheManager() {
      override fun createConcurrentMapCache(@NonNull name: String): Cache {
        return ConcurrentMapCache(
          name,
          CacheBuilder.newBuilder()
            .expireAfterWrite(1000L, TimeUnit.MINUTES)
            .maximumSize(1000L).build<Any, Any>().asMap(),
          false
        )
      }
    }
  }

  @Bean
  override fun cacheResolver(): CacheResolver {
    val cacheManager = Objects.requireNonNull(cacheManager())
    return SimpleCacheResolver(cacheManager!!)
  }

  override fun keyGenerator(): KeyGenerator {
    return SimpleKeyGenerator()
  }

  override fun errorHandler(): CacheErrorHandler {
    return SimpleCacheErrorHandler()
  }
}