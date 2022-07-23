package com.toy.redisson.test

import com.toy.redisson.test.config.RedissonConfig
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.redisson.api.RedissonReactiveClient

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class BaseTest {

  private val redissonConfig: RedissonConfig = RedissonConfig(null)
  protected lateinit var client: RedissonReactiveClient

  @BeforeAll
  fun setup() {
    client = redissonConfig.getReactiveClient()
  }

  @AfterAll
  fun clear() {
    client.shutdown()
  }
}