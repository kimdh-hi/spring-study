package com.toy.springcacheex.config

import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.testcontainers.shaded.org.apache.commons.lang3.StringUtils
import redis.embedded.RedisServer
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy


//@Configuration
//@Profile("local")
class EmbeddedRedisConfig(
  private val redisProperties: RedisProperties
) {

  private val log = LoggerFactory.getLogger(javaClass)

  lateinit var redisServer: RedisServer

//  @PostConstruct
  fun startEmbeddedRedis() {
    redisServer = RedisServer(redisProperties.port)

    try {
      redisServer.start()
      log.debug("Embedded redis start")
    } catch (e: Exception) {
      log.error("Embedded redis error")
    }
  }

//  @PreDestroy
  fun stopEmbeddedRedis() {
    redisServer.stop()
    log.debug("Embedded redis stop")
  }
}