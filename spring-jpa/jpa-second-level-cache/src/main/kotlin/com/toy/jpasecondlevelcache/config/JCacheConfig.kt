package com.toy.jpasecondlevelcache.config

import com.toy.jpasecondlevelcache.domain.SomeEntity
import org.ehcache.jsr107.EhcacheCachingProvider
import org.hibernate.cache.jcache.ConfigSettings
import org.hibernate.cfg.AvailableSettings
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit
import javax.cache.configuration.MutableConfiguration
import javax.cache.expiry.CreatedExpiryPolicy
import javax.cache.expiry.Duration

@Configuration
@ConditionalOnProperty(
  prefix = "spring.jpa.properties",
  name = [AvailableSettings.USE_SECOND_LEVEL_CACHE],
  havingValue = "true"
)
class JCacheConfig {

  @Bean
  fun hibernatePropertiesCustomizer(): HibernatePropertiesCustomizer {
    return HibernatePropertiesCustomizer { properties: MutableMap<String?, Any?> ->
      properties[ConfigSettings.CACHE_MANAGER] = jCacheCacheManager()
    }
  }

  private fun jCacheCacheManager() {
    val cacheManager = EhcacheCachingProvider().cacheManager
    SecondLevelCachePolicy.policyMap.keys.associateWith { key ->
      cacheManager.createCache(key, getConfiguration(SecondLevelCachePolicy.policyMap[key]!!))
    }
  }
}

private fun getConfiguration(duration: Duration): MutableConfiguration<String, Any> {
  return MutableConfiguration<String, Any>()
    .apply { setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(duration)) }
}

object SecondLevelCachePolicy {
  val policyMap = mapOf(
    SomeEntity::class.qualifiedName to Duration(TimeUnit.MINUTES, 30)
  )
}