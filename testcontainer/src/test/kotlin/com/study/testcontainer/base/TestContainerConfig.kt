package com.study.testcontainer.base

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.utility.DockerImageName

@TestConfiguration(proxyBeanMethods = false)
class TestContainerConfig {

  @Bean
  @ServiceConnection(name = "redis")
  fun redisContainer(): GenericContainer<*> {
    return GenericContainer(DockerImageName.parse("redis:7.0.12")).withExposedPorts(6379)
  }

  @Bean
  @ServiceConnection
  fun mySQLContainer() = MySQLContainer(DockerImageName.parse("mysql:8.4.4"))
}
