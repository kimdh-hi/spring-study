package com.study.jwtkeyrotate.ui

import com.study.jwtkeyrotate.application.JwtKeyService
import com.study.jwtkeyrotate.domain.model.JwtKey
import com.study.jwtkeyrotate.domain.model.enums.JwtKeyStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/jwt-keys")
class JwtKeyController(private val keyService: JwtKeyService) {
  @GetMapping
  fun list() = keyService.list().map(KeyResponse::from)

  @PostMapping("/rotate")
  fun rotate() = RotateResponse(keyService.rotate().kid!!)

  @DeleteMapping("/{kid}")
  fun revoke(@PathVariable kid: String) = keyService.revoke(kid)

  data class RotateResponse(val kid: String)

  data class KeyResponse(val kid: String, val status: JwtKeyStatus, val createdAt: Instant, val demotedAt: Instant?) {
    companion object {
      fun from(key: JwtKey) = KeyResponse(key.kid!!, key.status, key.createdAt, key.demotedAt)
    }
  }
}
