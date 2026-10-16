package com.toy.springsse

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class SseHeartbeat(
  private val registry: SseEmitterRegistry,
) {

  @Scheduled(fixedRate = 15_000)
  fun ping() = registry.heartbeat()
}
