package com.toy.oauthclientoidc.controller

import com.toy.oauthclientoidc.auth.Oauth2TokenResponseVO
import com.toy.oauthclientoidc.auth.Oauth2TokenService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class TestController(
  private val oauth2TokenService: Oauth2TokenService
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @GetMapping("/o2auth2/{registrationId}")
  fun googleRedirect(
    @PathVariable registrationId: String,
    @RequestParam code: String,
  ): ResponseEntity<Mono<Oauth2TokenResponseVO>> {
    log.info("registrationId: {}, code: {}", registrationId, code)
    val responseVO = oauth2TokenService.getToken(registrationId, code)

    return ResponseEntity.ok(responseVO)
  }
}