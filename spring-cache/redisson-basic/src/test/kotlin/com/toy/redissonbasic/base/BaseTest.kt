package com.toy.redissonbasic.base

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.redisson.api.RedissonReactiveClient

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class BaseTest {

  private val redissonConfig = RedissonConfig()
  protected var client: RedissonReactiveClient? = null

  @BeforeAll
  fun beforeAll() {
    client = redissonConfig.getReactiveClient()
  }

  @AfterAll
  fun afterAll() {
    client?.shutdown()
  }
}