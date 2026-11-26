package com.toy.springmfa.application

import com.toy.springmfa.infra.security.webauthn.CeremonyOptionsRepository
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.FactorGrantedAuthority
import org.springframework.security.web.webauthn.api.AuthenticatorAssertionResponse
import org.springframework.security.web.webauthn.api.PublicKeyCredential
import org.springframework.security.web.webauthn.api.PublicKeyCredentialCreationOptions
import org.springframework.security.web.webauthn.api.PublicKeyCredentialRequestOptions
import org.springframework.security.web.webauthn.authentication.WebAuthnAuthenticationRequestToken
import org.springframework.security.web.webauthn.management.ImmutablePublicKeyCredentialCreationOptionsRequest
import org.springframework.security.web.webauthn.management.ImmutablePublicKeyCredentialRequestOptionsRequest
import org.springframework.security.web.webauthn.management.ImmutableRelyingPartyRegistrationRequest
import org.springframework.security.web.webauthn.management.PublicKeyCredentialUserEntityRepository
import org.springframework.security.web.webauthn.management.RelyingPartyAuthenticationRequest
import org.springframework.security.web.webauthn.management.RelyingPartyPublicKey
import org.springframework.security.web.webauthn.management.UserCredentialRepository
import org.springframework.security.web.webauthn.management.WebAuthnRelyingPartyOperations
import org.springframework.stereotype.Service

@Service
class PasskeyService(
  private val webAuthnRelyingPartyOperations: WebAuthnRelyingPartyOperations,
  private val publicKeyCredentialUserEntityRepository: PublicKeyCredentialUserEntityRepository,
  private val userCredentialRepository: UserCredentialRepository,
  private val publicKeyCredentialCreationOptionsRepository: CeremonyOptionsRepository<PublicKeyCredentialCreationOptions>,
  private val publicKeyCredentialRequestOptionsRepository: CeremonyOptionsRepository<PublicKeyCredentialRequestOptions>,
  private val factorTokenService: FactorTokenService,
) {

  fun hasPasskey(username: String): Boolean {
    val userEntity = publicKeyCredentialUserEntityRepository.findByUsername(username) ?: return false
    return userCredentialRepository.findByUserId(userEntity.id).isNotEmpty()
  }

  fun registrationOptions(authentication: Authentication): PublicKeyCredentialCreationOptions {
    checkRegistrable(authentication)
    return webAuthnRelyingPartyOperations
      .createPublicKeyCredentialCreationOptions(ImmutablePublicKeyCredentialCreationOptionsRequest(authentication))
      .also { publicKeyCredentialCreationOptionsRepository.save(authentication.name, it) }
  }

  fun register(authentication: Authentication, publicKey: RelyingPartyPublicKey) {
    checkRegistrable(authentication)
    val options = publicKeyCredentialCreationOptionsRepository.getAndDelete(authentication.name)
      ?: throw BadCredentialsException("no pending registration")
    webAuthnRelyingPartyOperations.registerCredential(ImmutableRelyingPartyRegistrationRequest(options, publicKey))
  }

  fun authenticationOptions(authentication: Authentication): PublicKeyCredentialRequestOptions =
    webAuthnRelyingPartyOperations
      .createCredentialRequestOptions(ImmutablePublicKeyCredentialRequestOptionsRequest(authentication))
      .also { publicKeyCredentialRequestOptionsRepository.save(authentication.name, it) }

  fun authenticate(
    authentication: Authentication,
    credential: PublicKeyCredential<AuthenticatorAssertionResponse>,
  ): String {
    val options = publicKeyCredentialRequestOptionsRepository.getAndDelete(authentication.name) ?: throw BadCredentialsException("no pending authentication")
    val request = WebAuthnAuthenticationRequestToken(RelyingPartyAuthenticationRequest(options, credential))
    return factorTokenService.stepUp(authentication, request)
  }

  private fun checkRegistrable(authentication: Authentication) {
    if (hasPasskey(authentication.name) && !hasWebAuthnFactor(authentication)) {
      throw AccessDeniedException("passkey already registered, authenticate with it first")
    }
  }

  private fun hasWebAuthnFactor(authentication: Authentication): Boolean =
    authentication.authorities.any { it.authority == FactorGrantedAuthority.WEBAUTHN_AUTHORITY }
}
