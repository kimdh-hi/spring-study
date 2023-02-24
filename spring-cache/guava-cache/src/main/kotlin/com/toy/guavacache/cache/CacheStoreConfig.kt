package com.toy.guavacache.cache

import com.toy.guavacache.domain.TestData
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@Configuration
class CacheStoreConfig {

  @Bean
  fun testDataStore(): CacheStore<TestData> {
    return CacheStore(10, TimeUnit.SECONDS)
  }
}