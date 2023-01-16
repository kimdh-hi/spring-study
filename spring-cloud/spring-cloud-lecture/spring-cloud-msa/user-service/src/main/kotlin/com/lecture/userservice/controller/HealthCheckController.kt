package com.lecture.userservice.controller

import org.springframework.core.env.Environment
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthCheckController(
  private val env: Environment
) {

  @GetMapping("/health-check")
  fun healthCheck() = """
    user service ok port: ${env.getProperty("local.server.port")}"
    token.secret: ${env.getProperty("token.secret")}
    token.expirationTime: ${env.getProperty("token.expiration_time")}
  """.trimIndent()
}