package com.toy.springmfa.config

import com.toy.springmfa.auth.passkey.PendingCeremonyStore
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.web.webauthn.api.PublicKeyCredentialCreationOptions
import org.springframework.security.web.webauthn.api.PublicKeyCredentialRequestOptions
import org.springframework.security.web.webauthn.api.PublicKeyCredentialRpEntity
import org.springframework.security.web.webauthn.jackson.WebauthnJacksonModule
import org.springframework.security.web.webauthn.management.PublicKeyCredentialUserEntityRepository
import org.springframework.security.web.webauthn.management.UserCredentialRepository
import org.springframework.security.web.webauthn.management.WebAuthnRelyingPartyOperations
import org.springframework.security.web.webauthn.management.Webauthn4JRelyingPartyOperations
import java.time.Clock
import java.time.Duration

@Configuration
class WebAuthnConfig(
  private val properties: MfaProperties,
  private val clock: Clock,
) {

  @Bean
  fun relyingPartyOperations(
    userEntityRepository: PublicKeyCredentialUserEntityRepository,
    userCredentialRepository: UserCredentialRepository,
  ): WebAuthnRelyingPartyOperations =
    Webauthn4JRelyingPartyOperations(
      userEntityRepository,
      userCredentialRepository,
      PublicKeyCredentialRpEntity.builder()
        .id(properties.webauthn.rpId)
        .name(properties.webauthn.rpName)
        .build(),
      properties.webauthn.allowedOrigins,
    )

  @Bean
  fun webauthnJacksonModule() = WebauthnJacksonModule()

  @Bean
  fun creationOptionsStore() = PendingCeremonyStore<PublicKeyCredentialCreationOptions>(CEREMONY_TTL, clock)

  @Bean
  fun requestOptionsStore() = PendingCeremonyStore<PublicKeyCredentialRequestOptions>(CEREMONY_TTL, clock)

  companion object {
    private val CEREMONY_TTL: Duration = Duration.ofMinutes(5)
  }
}
