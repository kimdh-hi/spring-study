package com.toy.oauth2clientcredentials.infra.security

import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import com.toy.oauth2clientcredentials.domain.repository.OAuthClientRepository
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator
import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.security.oauth2.core.OAuth2ErrorCodes
import org.springframework.security.oauth2.core.OAuth2TokenValidator
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtValidators
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.stereotype.Component
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.UUID

@Component
class ClientJwtCodec(
  private val properties: SecurityProperties,
  private val clientRepository: OAuthClientRepository,
) {
  val jwkSource: JWKSource<SecurityContext> = generateRsaJwkSource()

  fun decoder(): JwtDecoder =
    NimbusJwtDecoder.withJwkSource(jwkSource).build().apply {
      setJwtValidator(
        DelegatingOAuth2TokenValidator(
          JwtValidators.createDefaultWithIssuer(properties.issuer),
          OAuth2TokenValidator(::validateClient),
        ),
      )
    }

  private fun validateClient(jwt: Jwt): OAuth2TokenValidatorResult {
    val clientId = jwt.subject ?: return invalid("missing client")
    return if (clientRepository.existsByClientId(clientId)) OAuth2TokenValidatorResult.success() else invalid("client is not enabled")
  }
}

@Component
class AdminJwtCodec(properties: SecurityProperties) {
  val issuer = "${properties.issuer}/admin"
  private val jwkSource = generateRsaJwkSource()
  val encoder: JwtEncoder = NimbusJwtEncoder(jwkSource)

  fun decoder(): JwtDecoder =
    NimbusJwtDecoder.withJwkSource(jwkSource).build().apply {
      setJwtValidator(JwtValidators.createDefaultWithIssuer(issuer))
    }
}

private fun invalid(description: String) =
  OAuth2TokenValidatorResult.failure(OAuth2Error(OAuth2ErrorCodes.INVALID_TOKEN, description, null))

private fun generateRsaJwkSource(): JWKSource<SecurityContext> {
  val keyPair = KeyPairGenerator.getInstance("RSA").apply { initialize(2048) }.generateKeyPair()
  val rsaKey = RSAKey.Builder(keyPair.public as RSAPublicKey)
    .privateKey(keyPair.private as RSAPrivateKey)
    .keyID(UUID.randomUUID().toString())
    .build()
  return ImmutableJWKSet(JWKSet(rsaKey))
}
