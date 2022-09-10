package com.toy.redissonbasic

import com.toy.redissonbasic.base.BaseTest
import com.toy.redissonbasic.base.Student
import org.junit.jupiter.api.Test
import org.redisson.codec.JsonJacksonCodec
import org.redisson.codec.TypedJsonJacksonCodec
import reactor.test.StepVerifier

class `02-KeyValue-Object`: BaseTest() {

  @Test
  fun `object value`() {
    // 1. Serializable 구현
    // 2. JsonJacksonCodec 설정 - @class 프로퍼티에 패키지 경로가 추가로 저장되고 해당 패키지 경로의 객체로 역직렬화를 시도함
    //                        @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS) 의 추가 설정을 필요로 함
    // 3. (권장) TypedJsonJacksonCode 설정 - @class 없음
    val bucket = client!!.getBucket<Student>("student:1", TypedJsonJacksonCodec(Student::class.java))
    val student = Student.test()
    val set = bucket.set(student)
    val get = bucket.get()
      .doOnNext { println(it) }
      .then()
    StepVerifier.create(set.concatWith(get))
      .verifyComplete()
  }
}