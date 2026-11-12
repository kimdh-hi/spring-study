package com.toy.springmfa.auth.passkey

import com.toy.springmfa.auth.FactorTokenService
import com.toy.springmfa.auth.TokenResponse
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.security.web.webauthn.api.AuthenticatorAssertionResponse
import org.springframework.security.web.webauthn.api.PublicKeyCredential
import org.springframework.security.web.webauthn.api.PublicKeyCredentialCreationOptions
import org.springframework.security.web.webauthn.api.PublicKeyCredentialRequestOptions
import org.springframework.security.web.webauthn.authentication.WebAuthnAuthenticationRequestToken
import org.springframework.security.web.webauthn.management.ImmutablePublicKeyCredentialCreationOptionsRequest
import org.springframework.security.web.webauthn.management.ImmutablePublicKeyCredentialRequestOptionsRequest
import org.springframework.security.web.webauthn.management.ImmutableRelyingPartyRegistrationRequest
import org.springframework.security.web.webauthn.management.RelyingPartyAuthenticationRequest
import org.springframework.security.web.webauthn.management.RelyingPartyPublicKey
import org.springframework.security.web.webauthn.management.WebAuthnRelyingPartyOperations
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/mfa/passkeys")
class PasskeyController(
  private val relyingPartyOperations: WebAuthnRelyingPartyOperations,
  private val creationOptionsStore: PendingCeremonyStore<PublicKeyCredentialCreationOptions>,
  private val requestOptionsStore: PendingCeremonyStore<PublicKeyCredentialRequestOptions>,
  private val factorTokenService: FactorTokenService,
) {

  @PostMapping("/registration/options")
  fun registrationOptions(authentication: Authentication): PublicKeyCredentialCreationOptions =
    relyingPartyOperations
      .createPublicKeyCredentialCreationOptions(ImmutablePublicKeyCredentialCreationOptionsRequest(authentication))
      .also { creationOptionsStore.save(authentication.name, it) }

  @PostMapping("/registration")
  @ResponseStatus(HttpStatus.CREATED)
  fun register(authentication: Authentication, @RequestBody request: RegistrationRequest) {
    val options = creationOptionsStore.take(authentication.name) ?: throw BadCredentialsException("no pending registration")
    relyingPartyOperations.registerCredential(ImmutableRelyingPartyRegistrationRequest(options, request.publicKey))
  }

  @PostMapping("/authentication/options")
  fun authenticationOptions(authentication: Authentication): PublicKeyCredentialRequestOptions =
    relyingPartyOperations
      .createCredentialRequestOptions(ImmutablePublicKeyCredentialRequestOptionsRequest(authentication))
      .also { requestOptionsStore.save(authentication.name, it) }

  @PostMapping("/authentication")
  fun authenticate(
    authentication: Authentication,
    @RequestBody credential: PublicKeyCredential<AuthenticatorAssertionResponse>,
  ): TokenResponse {
    val options = requestOptionsStore.take(authentication.name) ?: throw BadCredentialsException("no pending authentication")
    val request = WebAuthnAuthenticationRequestToken(RelyingPartyAuthenticationRequest(options, credential))
    return TokenResponse(factorTokenService.stepUp(authentication, request))
  }

  data class RegistrationRequest(val publicKey: RelyingPartyPublicKey)
}
