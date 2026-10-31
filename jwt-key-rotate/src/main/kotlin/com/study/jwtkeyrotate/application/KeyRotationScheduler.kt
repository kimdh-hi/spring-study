package com.study.jwtkeyrotate.application

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class KeyRotationScheduler(private val keyService: JwtKeyService) : ApplicationRunner {
  override fun run(args: ApplicationArguments) {
    keyService.init()
  }

  @Scheduled(cron = "\${jwt.rotation-cron}")
  fun rotate() {
    keyService.rotate()
  }

  @Scheduled(cron = "0 0 * * * *")
  fun cleanup() {
    keyService.cleanup()
  }
}
