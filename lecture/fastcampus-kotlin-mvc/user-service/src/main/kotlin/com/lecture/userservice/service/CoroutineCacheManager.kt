package com.lecture.userservice.service

import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap

@Component
class CoroutineCacheManager<T> {

  private val localCache = ConcurrentHashMap<String, CacheWrapper<T>>()

  suspend fun put(key: String, value: T, ttl: Duration) {
    localCache[key] = CacheWrapper(value, Instant.now().plusMillis(ttl.toMillis()))
  }

  suspend fun evict(key: String) {
    localCache.remove(key)
  }

  data class CacheWrapper<T>(val data: T, val ttl: Instant)
}