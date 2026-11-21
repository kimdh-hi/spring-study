package com.toy.oauth2clientcredentials.infra.security.authserver

import java.security.KeyFactory
import java.security.interfaces.RSAPublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

object RsaPublicKeys {
  fun parse(pem: String): RSAPublicKey {
    val body = pem.lines().filterNot { it.startsWith("-----") }.joinToString("").trim()
    val spec = X509EncodedKeySpec(Base64.getMimeDecoder().decode(body))
    return KeyFactory.getInstance("RSA").generatePublic(spec) as RSAPublicKey
  }
}
