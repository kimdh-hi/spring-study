package com.lecture.fsmysql.utils

import com.lecture.fsmysql.domain.member.entity.Member
import org.jeasy.random.EasyRandom
import org.jeasy.random.EasyRandomParameters
import org.junit.jupiter.api.Test
import java.util.stream.LongStream

object MemberFixtureFactory {

  fun create(seed: Long = 1): Member {
    val easyRandomParameters = EasyRandomParameters().seed(seed)
    return EasyRandom(easyRandomParameters)
      .nextObject(Member::class.java)
  }
}

internal class EasyRandomTest {

  @Test
  fun memberCreateTest() {
    LongStream.range(0, 10)
      .mapToObj { MemberFixtureFactory.create(it) }
      .forEach { println(it) }
  }
}