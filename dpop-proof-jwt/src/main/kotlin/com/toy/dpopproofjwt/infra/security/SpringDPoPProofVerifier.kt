package com.toy.dpopproofjwt.infra.security

import com.nimbusds.jose.JWSObject
import com.toy.dpopproofjwt.application.auth.DPoPProof
import com.toy.dpopproofjwt.application.auth.DPoPProofVerifier
import com.toy.dpopproofjwt.common.exception.InvalidDPoPProofException
import com.toy.dpopproofjwt.common.exception.UseDPoPNonceException
import org.springframework.security.oauth2.jwt.DPoPProofContext
import org.springframework.security.oauth2.jwt.JwtDecoderFactory
import org.springframework.stereotype.Component

@Component
class SpringDPoPProofVerifier(
  private val dPoPProofDecoderFactory: JwtDecoderFactory<DPoPProofContext>,
) : DPoPProofVerifier {

  override fun verify(proof: DPoPProof): String {
    try {
      val context = DPoPProofContext.withDPoPProof(proof.value)
        .method(proof.method)
        .targetUri(proof.targetUri)
        .build()

      dPoPProofDecoderFactory.createDecoder(context).decode(proof.value)
    } catch (ex: Exception) {
      if (ex.isUseDPoPNonce()) throw UseDPoPNonceException(ex)
      throw InvalidDPoPProofException(ex.message, ex)
    }

    return JWSObject.parse(proof.value).header.jwk.computeThumbprint().toString()
  }
}
