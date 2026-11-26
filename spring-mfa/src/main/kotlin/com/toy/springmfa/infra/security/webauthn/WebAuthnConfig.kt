package com.toy.springmfa.infra.security.webauthn

import com.toy.springmfa.infra.config.MfaProperties
import java.time.Duration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import org.springframework.security.web.webauthn.api.PublicKeyCredentialCreationOptions
import org.springframework.security.web.webauthn.api.PublicKeyCredentialRequestOptions
import org.springframework.security.web.webauthn.api.PublicKeyCredentialRpEntity
import org.springframework.security.web.webauthn.jackson.WebauthnJacksonModule
import org.springframework.security.web.webauthn.management.PublicKeyCredentialUserEntityRepository
import org.springframework.security.web.webauthn.management.UserCredentialRepository
import org.springframework.security.web.webauthn.management.WebAuthnRelyingPartyOperations
import org.springframework.security.web.webauthn.management.Webauthn4JRelyingPartyOperations

@Configuration
class WebAuthnConfig(
  private val mfaProperties: MfaProperties,
) {

  @Bean
  fun webAuthnRelyingPartyOperations(
    publicKeyCredentialUserEntityRepository: PublicKeyCredentialUserEntityRepository,
    userCredentialRepository: UserCredentialRepository,
  ): WebAuthnRelyingPartyOperations =
    Webauthn4JRelyingPartyOperations(
      publicKeyCredentialUserEntityRepository,
      userCredentialRepository,
      PublicKeyCredentialRpEntity.builder()
        .id(mfaProperties.webauthn.rpId)
        .name(mfaProperties.webauthn.rpName)
        .build(),
      mfaProperties.webauthn.allowedOrigins,
    )

  @Bean
  fun webauthnJacksonModule() = WebauthnJacksonModule()

  @Bean
  fun ceremonyRedisTemplate(redisConnectionFactory: RedisConnectionFactory) =
    RedisTemplate<String, Any>().apply {
      this.connectionFactory = redisConnectionFactory
      keySerializer = StringRedisSerializer.UTF_8
      valueSerializer = JdkSerializationRedisSerializer()
    }

  @Bean
  fun publicKeyCredentialCreationOptionsRepository(ceremonyRedisTemplate: RedisTemplate<String, Any>) =
    CeremonyOptionsRepository(
      ceremonyRedisTemplate,
      PublicKeyCredentialCreationOptions::class.java,
      "webauthn:ceremony:registration",
      CEREMONY_TTL,
    )

  @Bean
  fun publicKeyCredentialRequestOptionsRepository(ceremonyRedisTemplate: RedisTemplate<String, Any>) =
    CeremonyOptionsRepository(
      ceremonyRedisTemplate,
      PublicKeyCredentialRequestOptions::class.java,
      "webauthn:ceremony:authentication",
      CEREMONY_TTL,
    )

  companion object {
    private val CEREMONY_TTL: Duration = Duration.ofMinutes(5)
  }
}
