package com.toy.webfluxcache.service

import com.toy.webfluxcache.config.cache.ReactorCache
import com.toy.webfluxcache.vo.TestResponseVO
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class TestService {

  private val log = LoggerFactory.getLogger(javaClass)

  fun service(): Mono<TestResponseVO> {
    log.info("service called ...")

    return Mono.just(TestResponseVO("data"))
  }

  @Cacheable("test")
  fun suspendServiceAsync(): Deferred<TestResponseVO> = CoroutineScope(Dispatchers.Default).async {
    log.info("service called ...")
    return@async TestResponseVO("data")
  }

  @ReactorCache("test")
  fun serviceReactive(): Mono<TestResponseVO> {
    log.info("service called ...")

    return Mono.just(TestResponseVO("data"))
  }
}