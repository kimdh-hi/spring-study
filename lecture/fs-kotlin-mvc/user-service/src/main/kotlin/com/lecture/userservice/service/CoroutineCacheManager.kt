package com.lecture.userservice.service

import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap
import java.util.function.Supplier

@Component
class CoroutineCacheManager<T : Any> {

  private val localCache = ConcurrentHashMap<String, CachedData<T>>()

  suspend fun put(key: String, value: T, ttl: Duration) {
    localCache[key] = CachedData(value, Instant.now().plusMillis(ttl.toMillis()))
  }

  suspend fun evict(key: String) {
    localCache.remove(key)
  }

  suspend fun getOrPut(
    key: String,
    ttl: Duration,
    supplier: suspend () -> T,
  ): T {
    val now = Instant.now()
    val cachedData = localCache[key]
    val cache = if (cachedData == null) {
      CachedData(data = supplier(), ttl = now.plusMillis(ttl.toMillis())).also {
        localCache[key] = it
      }
    } else if (now.isAfter(cachedData.ttl)) {
      localCache.remove(key)
      CachedData(data = supplier(), ttl = now.plusMillis(ttl.toMillis())).also {
        localCache[key] = it
      }
    } else {
      cachedData
    }
    checkNotNull(cache.data)
    return cache.data
  }

  data class CachedData<T>(val data: T, val ttl: Instant)
}