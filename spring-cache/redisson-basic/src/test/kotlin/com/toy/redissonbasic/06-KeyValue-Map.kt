package com.toy.redissonbasic

import com.toy.redissonbasic.base.BaseTest
import com.toy.redissonbasic.base.Student
import org.junit.jupiter.api.Test
import org.redisson.client.codec.StringCodec
import org.redisson.codec.TypedJsonJacksonCodec
import reactor.test.StepVerifier

/**
 * hgetall user:1
 * hget user:1 name
 */
class `06-KeyValue-Map`: BaseTest() {

  @Test
  fun `put`() {
    val map = client!!.getMap<String, String>("user:1", StringCodec.INSTANCE)
    val name = map.put("name", "test-name")
    val age = map.put("age", "27")
    val country = map.put("country", "kr")

    StepVerifier.create(name.concatWith(age).concatWith(country))
      .verifyComplete()
  }

  @Test
  fun `putAll`() {
    val bucketMap = client!!.getMap<String, String>("user:2", StringCodec.INSTANCE)
    val map = mapOf(
      "name" to "test-name2",
      "age" to "20",
      "country" to "usa"
    )
    val putMono = bucketMap.putAll(map).then()
    StepVerifier.create(putMono)
      .verifyComplete()
  }

  @Test
  fun `object value`() {
    val jsonJacksonCodec = TypedJsonJacksonCodec(Int::class.java, Student::class.java)
    val bucketMap = client!!.getMap<Int, Student>("users", jsonJacksonCodec)

    val student1 = Student("student1", 20, "city1", listOf(1,2,3))
    val student2 = Student("student2", 21, "city2", listOf(1,2,3))
    val student3 = Student("student3", 22, "city3", listOf(1,2,3))

    val student1Map =  bucketMap.put(1, student1).then()
    val student2Map = bucketMap.put(2, student2).then()
    val student3Map = bucketMap.put(3, student3).then()

    StepVerifier.create(student1Map.concatWith(student2Map).concatWith(student3Map))
      .verifyComplete()
  }
}