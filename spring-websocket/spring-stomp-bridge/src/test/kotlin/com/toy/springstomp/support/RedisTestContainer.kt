package com.toy.springstomp.support

import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

private const val REDIS_PORT = 6379

@Testcontainers
@ContextConfiguration
abstract class RedisTestContainer {
  companion object {
    @Container
    @ServiceConnection(name = "redis")
    @JvmStatic
    val redis: GenericContainer<*> = GenericContainer(DockerImageName.parse("redis:7.4-alpine"))
      .withExposedPorts(REDIS_PORT)
  }
}
