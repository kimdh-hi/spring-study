package com.toy.springcacheex.config

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import redis.embedded.RedisServer
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy


@Configuration
@Profile("local")
class EmbeddedRedisConfig(
  private val redisProperties: RedisProperties
) {

  private val log = LoggerFactory.getLogger(javaClass)

  lateinit var redisServer: RedisServer

  @Bean
  fun redisServer(redisProperties: RedisProperties): RedisServer {
    val port = redisProperties.port
    return RedisServer(port)
  }

  @PostConstruct
  fun startRedisServer() {
    redisServer = RedisServer(redisProperties.port)
    try {
      redisServer.start()
      log.debug("Embedded redis start")
    } catch (e: Exception) {
      log.error("Embedded redis error")
    }
  }

  @PreDestroy
  fun stopRedisServer() {
    redisServer.stop()
    log.debug("Embedded redis stop")
  }
}