package com.toy.webfluxcache.service

import com.toy.webfluxcache.config.cache.ReactorCache
import com.toy.webfluxcache.repository.TestRepository
import com.toy.webfluxcache.vo.TestResponseVO
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class TestService(
  private val testRepository: TestRepository
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @Cacheable("testReactorDefault")
  fun serviceReactorDefault(): Mono<TestResponseVO> {
    log.info("service called ...")

    return Mono.just(TestResponseVO( "1", "data"))
  }

  @ReactorCache("testReactorCache")
  fun serviceReactorCache(): Mono<TestResponseVO> {
    log.info("service called ...")

    return Mono.just(TestResponseVO("1", "data"))
  }

  @Cacheable("testSuspendDefault")
  suspend fun suspendServiceDefault(): TestResponseVO {
    log.info("service called ...")
    return TestResponseVO("1", "data")
  }

  @Cacheable("testSuspendDeferred")
  fun suspendServiceAsync(): Deferred<TestResponseVO> = CoroutineScope(Dispatchers.Default).async {
    log.info("service called ...")
    val testEntity = testRepository.findById("1")
    return@async TestResponseVO.of(testEntity)
  }
}