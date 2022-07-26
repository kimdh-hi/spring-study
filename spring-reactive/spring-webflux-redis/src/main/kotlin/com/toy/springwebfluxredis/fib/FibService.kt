package com.toy.springwebfluxredis.fib

import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class FibService {

  // key가 지정되지 않은 경우 모든 parameter 가 key 를 위한 hash 값으로 사용됨.
  @Cacheable(value = ["math:fib"], key = "#n")
  fun getFib(n: Int, name: String): Int {
    println("calculating... n=$n, name=$name")
    return fib(n)
  }

  @CacheEvict(value = ["math:fib"], key = "#n")
  fun clearCache(n: Int) {
    println("clear cache...key=$n")
  }

  @Scheduled(fixedRate = 10 * 1000)
  @CacheEvict(value = ["math:fib"], allEntries = true)
  fun clearCacheScheduler() {
    println("clear cache...")
  }

  private fun fib(n: Int): Int {
    if (n < 2)
      return n
    return fib(n-1) + fib(n-2)
  }
}