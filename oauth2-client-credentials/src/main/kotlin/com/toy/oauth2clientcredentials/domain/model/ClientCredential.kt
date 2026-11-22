package com.toy.oauth2clientcredentials.domain.model

import com.toy.oauth2clientcredentials.common.exception.InvalidClientMetadataException
import java.net.URI
import java.security.KeyFactory
import java.security.interfaces.RSAPublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

sealed interface ClientCredential {
  val authMethod: ClientAuthMethod

  data class Secret(val hash: String) : ClientCredential {
    override val authMethod get() = ClientAuthMethod.SECRET
  }

  sealed interface PublicKey : ClientCredential {
    override val authMethod get() = ClientAuthMethod.PRIVATE_KEY_JWT
  }

  data class Pem(val pem: String) : PublicKey {
    val rsaPublicKey: RSAPublicKey = parseRsaPublicKey(pem)
      ?: throw InvalidClientMetadataException("valid RSA publicKey (X.509 PEM) is required")
  }

  data class JwkSetUrl(val url: String) : PublicKey {
    init {
      if (!isHttpUrl(url)) throw InvalidClientMetadataException("valid jwkSetUrl is required")
    }
  }
}

private fun parseRsaPublicKey(pem: String): RSAPublicKey? = runCatching {
  val body = pem.lines().filterNot { it.startsWith("-----") }.joinToString("")
  val spec = X509EncodedKeySpec(Base64.getMimeDecoder().decode(body))
  KeyFactory.getInstance("RSA").generatePublic(spec) as RSAPublicKey
}.getOrNull()

private fun isHttpUrl(url: String): Boolean =
  runCatching { URI(url).scheme }.getOrNull() in setOf("http", "https")
