package com.toy.reactivejdsl.common.cache

import org.springframework.cache.CacheManager
import reactor.cache.CacheMono
import reactor.core.publisher.Mono
import java.util.Objects

class ReactorCacheManager(
  private val cacheManager: CacheManager
) {
  fun <T> findCachedMono(cacheName: String, retriver: () -> Mono<*>, classType: Class<T>): Mono<*> {
    val cache = Objects.requireNonNull(cacheManager.getCache(cacheName))
    cache?.let {
      CacheMono.lookup(k -> {
        val result = it.get(k, classType)
      return Mono.justOrEmpty(result)
      })
    }

  }
}