package com.study.configclient

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.cloud.context.refresh.ContextRefresher
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled

@Configuration
@EnableScheduling
class ConfigPollingRefresher(
  private val contextRefresher: ContextRefresher
) {
  private val log = LoggerFactory.getLogger(javaClass)

  @Scheduled(
    initialDelayString = "10s",
    fixedDelayString = "10s"
  )
  fun poll() {
    runCatching { contextRefresher.refresh() }
      .onSuccess { if (it.isNotEmpty()) log.info("config refreshed: {}", it) }
      .onFailure { e -> log.warn("config refresh failed: {}", e.message) }
  }
}
