package com.toy.springmfa.ui

import com.toy.springmfa.application.PasskeyService
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.web.webauthn.api.AuthenticatorAssertionResponse
import org.springframework.security.web.webauthn.api.PublicKeyCredential
import org.springframework.security.web.webauthn.api.PublicKeyCredentialCreationOptions
import org.springframework.security.web.webauthn.api.PublicKeyCredentialRequestOptions
import org.springframework.security.web.webauthn.management.RelyingPartyPublicKey
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/mfa/passkeys")
class PasskeyController(
  private val passkeyService: PasskeyService,
) {

  @PostMapping("/registration/options")
  fun registrationOptions(authentication: Authentication): PublicKeyCredentialCreationOptions =
    passkeyService.registrationOptions(authentication)

  @PostMapping("/registration")
  @ResponseStatus(HttpStatus.CREATED)
  fun register(authentication: Authentication, @RequestBody request: RegistrationRequest) =
    passkeyService.register(authentication, request.publicKey)

  @PostMapping("/authentication/options")
  fun authenticationOptions(authentication: Authentication): PublicKeyCredentialRequestOptions =
    passkeyService.authenticationOptions(authentication)

  @PostMapping("/authentication")
  fun authenticate(
    authentication: Authentication,
    @RequestBody credential: PublicKeyCredential<AuthenticatorAssertionResponse>,
  ): TokenResponse = TokenResponse(passkeyService.authenticate(authentication, credential))

  data class RegistrationRequest(val publicKey: RelyingPartyPublicKey)
}
