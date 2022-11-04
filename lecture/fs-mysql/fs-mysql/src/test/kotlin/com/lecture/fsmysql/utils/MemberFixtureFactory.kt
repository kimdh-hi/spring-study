package com.lecture.fsmysql.utils

import com.lecture.fsmysql.domain.member.entity.Member
import org.jeasy.random.EasyRandom
import org.jeasy.random.EasyRandomParameters
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

object MemberFixtureFactory {

  fun create(): Member {
    val easyRandomParameters = EasyRandomParameters()
    return EasyRandom(easyRandomParameters)
      .nextObject(Member::class.java)
  }
}

internal class EasyRandomTest {

  @Test
  fun memberCreateTest() {
    val member = MemberFixtureFactory.create()

    println(member)
    assertNotNull(member.id)
  }
}