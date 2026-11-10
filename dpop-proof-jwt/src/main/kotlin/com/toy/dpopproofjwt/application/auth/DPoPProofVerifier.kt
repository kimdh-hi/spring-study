package com.toy.dpopproofjwt.application.auth

interface DPoPProofVerifier {
  fun verify(proof: DPoPProof): String
}

data class DPoPProof(
  val value: String,
  val method: String,
  val targetUri: String,
)
