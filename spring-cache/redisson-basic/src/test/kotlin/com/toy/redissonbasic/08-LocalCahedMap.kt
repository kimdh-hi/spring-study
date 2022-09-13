package com.toy.redissonbasic

import com.toy.redissonbasic.base.BaseTest
import com.toy.redissonbasic.base.RedissonConfig
import com.toy.redissonbasic.base.Student
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.redisson.api.LocalCachedMapOptions
import org.redisson.api.RLocalCachedMap
import org.redisson.codec.TypedJsonJacksonCodec
import reactor.core.publisher.Flux
import java.time.Duration

/**
 *
 * LocalCachedMap sync strategies
 * NONE: 현재 localCache 가 갱신됐을 때 그에 따라 redis 는 바로 갱신되지만 다른 app 의 localCache는 갱신되지 않는다.
 *       localCache 갱신을 다른 app(service)에 알리지 않는다.
 * INVALIDATE: 현재 localCacheMap 의 어떤 필드를 갱신했을 때 다른 모든 app 의 해당 필드를 invalid 시킨다
 *             다른 app 은 이후 요청 때 invalidate 된 부분에 대한 최신 데이터를 다시 캐싱한다.
 * UPDATE: 다른 app 의 localCache 를 바로 갱신한다.
 *
 * LocalCachedMap reconnect strategies
 * CLEAR: redis 연결이 어떤 이유로 끊어졌을 때, localCache 를 초기화한다.
 *        초기화 하는 이유는 다른 서비스로부터 localCache 가 갱신됐을 때 연결이 끊어진 app 의 localCache 는 갱신할 수 없기 때문이다
 * NONE
 */

class `08-LocalCahedMap`: BaseTest() {

  private var localCacheMap: RLocalCachedMap<Int, Student>? = null

  @BeforeAll
  fun setup() {
    val redissonConfig = RedissonConfig()
    val redissonClient = redissonConfig.getClient()

    val localCachedMapOptions = LocalCachedMapOptions.defaults<Int, Student>()
      .syncStrategy(LocalCachedMapOptions.SyncStrategy.UPDATE)
      .reconnectionStrategy(LocalCachedMapOptions.ReconnectionStrategy.NONE)

    localCacheMap = redissonClient.getLocalCachedMap(
      "students",
      TypedJsonJacksonCodec(Int::class.java, Student::class.java),
      localCachedMapOptions
    )
  }

  @Test
  fun `localCacheMap get`() {
    val student1 = Student(name = "name1", age = 20, city = "city1", testList = listOf(1,2,3))
    val student2 = Student(name = "name2", age = 20, city = "city2", testList = listOf(1,2,3,4))
    localCacheMap!![1] = student1
    localCacheMap!![2] = student2

    Flux.interval(Duration.ofSeconds(1))
      .doOnNext { i -> println("$i => ${localCacheMap!![1]}") }
      .subscribe()

    Thread.sleep(600000)
  }

  @Test
  fun `test`() {
    val updatedStudent = Student(name = "name1-update", age = 44, city = "city1-update", testList = listOf(1,2,3,4,5))
    localCacheMap!![1] = updatedStudent
  }
}