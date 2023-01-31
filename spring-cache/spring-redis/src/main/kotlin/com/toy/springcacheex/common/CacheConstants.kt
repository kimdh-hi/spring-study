package com.toy.springcacheex.common

import java.time.Duration

object CacheConstants {
  const val USER = "user"
  const val TEST_KEY1 = "testKey1"
  const val TEST_KEY2 = "testKey2"
  const val TEST_KEY3 = "testKey3"

  val expiresMap = mapOf(
    TEST_KEY1 to Duration.ofMinutes(10),
    TEST_KEY2 to Duration.ofMinutes(10),
    TEST_KEY3 to Duration.ofMinutes(10),
  )
}