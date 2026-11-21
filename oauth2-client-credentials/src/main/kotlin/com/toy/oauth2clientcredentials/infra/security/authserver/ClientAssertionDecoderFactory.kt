package com.toy.oauth2clientcredentials.infra.security.authserver

import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtDecoderFactory
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.server.authorization.authentication.JwtClientAssertionDecoderFactory
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient

class ClientAssertionDecoderFactory : JwtDecoderFactory<RegisteredClient> {

  override fun createDecoder(client: RegisteredClient): JwtDecoder {
    val pem = checkNotNull(client.clientSettings.getSetting<String>(PUBLIC_KEY_SETTING)) {
      "public key is not registered: ${client.clientId}"
    }
    return NimbusJwtDecoder.withPublicKey(RsaPublicKeys.parse(pem))
      .signatureAlgorithm(SignatureAlgorithm.RS256)
      .build()
      .apply { setJwtValidator(JwtClientAssertionDecoderFactory.DEFAULT_JWT_VALIDATOR_FACTORY.apply(client)) }
  }

  companion object {
    const val PUBLIC_KEY_SETTING = "settings.client.public-key-pem"
  }
}
