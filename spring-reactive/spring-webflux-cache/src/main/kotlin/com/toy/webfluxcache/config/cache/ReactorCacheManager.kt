package com.toy.webfluxcache.config.cache

import org.slf4j.LoggerFactory
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component
import reactor.cache.CacheMono
import reactor.core.publisher.Mono
import reactor.core.publisher.Signal
import java.util.function.BiFunction

@Component
class ReactorCacheManager(
  private val cacheManager: CacheManager
) {

  private val log = LoggerFactory.getLogger(javaClass)

  fun <T : Any> findCachedMono(cacheName: String, key: String, retriever: () -> Mono<*>, classType: Class<T>): Mono<*> {
    val cache = cacheManager.getCache(cacheName)!!
    return CacheMono.lookup({ k ->
      val result = cache.get(k, classType)
      Mono.justOrEmpty(result).map<Signal<*>> { Signal.next(it) }
    }, key)
      .onCacheMissResume(Mono.defer { retriever.invoke() })
      .andWriteWith(writer(cacheName))
  }

  fun writer(cacheName: String): BiFunction<String, Signal<out Any>, Mono<Void>> = BiFunction { key, signal ->
    Mono.fromRunnable {
      if(!signal.isOnError) {
        log.debug("ReactorCache[{}] put [{}]]", cacheName, key)
        cacheManager.getCache(cacheName)?.put(key, signal.get())
      }
    }
  }
}