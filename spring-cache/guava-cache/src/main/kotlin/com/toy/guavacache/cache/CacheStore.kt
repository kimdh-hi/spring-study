package com.toy.guavacache.cache

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import java.util.concurrent.TimeUnit

class CacheStore<T : Any>(
  private val expiryDuration: Long,
  private val timeUnit: TimeUnit
){

  val cache: Cache<String, T> by lazy {
    CacheBuilder.newBuilder()
      .expireAfterWrite(expiryDuration, timeUnit)
      .concurrencyLevel(Runtime.getRuntime().availableProcessors())
      .build()
  }

  fun get(key: String): T? {
    return cache.getIfPresent(key)
  }

  fun put(key: String, value: T) {
    cache.put(key, value)
  }
}