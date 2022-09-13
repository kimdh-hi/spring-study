package com.toy.redissonbasic

import com.toy.redissonbasic.base.BaseTest
import com.toy.redissonbasic.base.Student
import org.junit.jupiter.api.Test
import org.redisson.codec.TypedJsonJacksonCodec
import reactor.test.StepVerifier
import java.util.concurrent.TimeUnit

class `07-KeyValue-MapCache`: BaseTest() {

  @Test
  fun test() {
    val codec = TypedJsonJacksonCodec(Int::class.java, Student::class.java)
    val mapCache = client!!.getMapCache<Int, Student>("student:cache", codec)

    val student1 = Student(name = "name1", age = 20, city = "city1", testList = listOf(1,2,3))
    val student2 = Student(name = "name2", age = 21, city = "city2", testList = listOf(1,2,3))

    // MapCache 의 경우 ttl 설정 가능
    val put1 = mapCache.put(1, student1, 2, TimeUnit.SECONDS)
    val put2 = mapCache.put(2, student2, 10, TimeUnit.SECONDS)

    StepVerifier.create(put1.then().concatWith(put2.then()))
      .verifyComplete()

    Thread.sleep(3000L)

    mapCache.get(1).doOnNext { println(it) }.subscribe()
    mapCache.get(2).doOnNext { println(it) }.subscribe()

    Thread.sleep(3000L)

    mapCache.get(1).doOnNext { println(it) }.subscribe()
    mapCache.get(2).doOnNext { println(it) }.subscribe()
  }
}