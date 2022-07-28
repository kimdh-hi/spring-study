package com.toy.springconditional.config

abstract class CacheConfigInfo {
  abstract fun getName(): String
}

data class RedisConfigInfo (
  val cacheName: String = "REDIS"
): CacheConfigInfo() {
  override fun getName(): String = cacheName
}

data class EhcacheConfigInfo (
  val cacheName: String = "EHCACHE"
): CacheConfigInfo() {
  override fun getName(): String = cacheName
}

