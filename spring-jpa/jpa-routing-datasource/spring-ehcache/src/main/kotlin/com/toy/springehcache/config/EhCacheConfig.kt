package com.toy.springehcache.config

import com.toy.springehcache.vo.TestVO
import org.ehcache.config.CacheConfiguration
import org.ehcache.config.ResourcePools
import org.ehcache.config.builders.CacheConfigurationBuilder
import org.ehcache.config.builders.ExpiryPolicyBuilder
import org.ehcache.config.builders.ResourcePoolsBuilder
import org.ehcache.config.units.EntryUnit
import org.ehcache.config.units.MemoryUnit
import org.ehcache.core.config.DefaultConfiguration
import org.ehcache.jsr107.EhcacheCachingProvider
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CachingConfigurer
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.interceptor.*
import org.springframework.cache.jcache.JCacheCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration
import javax.cache.Caching

@Configuration
@EnableCaching
class EhCacheConfig: CachingConfigurer {

  companion object {
    const val MAX_HEAP_SIZE = 1000L
    const val MAX_OFF_HEAP_SIZE = 100L
  }

  @Bean
  override fun cacheManager(): CacheManager = JCacheCacheManager(createEhCacheManager())

  override fun cacheResolver(): CacheResolver = SimpleCacheResolver(cacheManager())

  override fun keyGenerator(): KeyGenerator = SimpleKeyGenerator()

  override fun errorHandler(): CacheErrorHandler = SimpleCacheErrorHandler()

  fun createEhCacheManager(): javax.cache.CacheManager {
    val cacheExpiresMap = getCacheExpiresMap()
    return getCacheManager(cacheExpiresMap)
  }

  private fun getCacheExpiresMap(): MutableMap<String, CacheConfiguration<*, *>> {
    val resourcePools = getResourcePools()
    return HashMap<String, CacheConfiguration<*, *>>().apply {
      getCacheConfig(TestVO::class.java, 5, 5, resourcePools)?.let { put("CACHE_TEST_VO", it) }
    }
  }

  private fun getResourcePools(): ResourcePools {
    return ResourcePoolsBuilder.newResourcePoolsBuilder()
      .heap(MAX_HEAP_SIZE, EntryUnit.ENTRIES)
      .offheap(MAX_OFF_HEAP_SIZE, MemoryUnit.MB)
      .build()
  }

  private fun getCacheConfig(
    valueType: Class<*>, timeToIdleSeconds: Long, timeToLiveSeconds: Long, resourcePools: ResourcePools
  ): CacheConfiguration<SimpleKey, out Any>? {
    return CacheConfigurationBuilder.newCacheConfigurationBuilder(
      SimpleKey::class.java, valueType, resourcePools
    )
      .withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofSeconds(timeToIdleSeconds)))
      .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(timeToLiveSeconds)))
      .build()
  }

  private fun getCacheManager(caches: MutableMap<String, CacheConfiguration<*, *>>): javax.cache.CacheManager {
    val provider: EhcacheCachingProvider =
      Caching.getCachingProvider("org.ehcache.jsr107.EhcacheCachingProvider") as EhcacheCachingProvider
    val configuration: org.ehcache.config.Configuration = DefaultConfiguration(caches, provider.defaultClassLoader)
    return provider.getCacheManager(provider.defaultURI, configuration)
  }
}