package com.toy.dpopproofjwt.common.exception

class InvalidCredentialsException : RuntimeException("Bad credentials.")

class InvalidDPoPProofException(message: String?, cause: Throwable? = null) : RuntimeException(message, cause)

class UseDPoPNonceException(cause: Throwable? = null) :
  RuntimeException("Authorization server requires nonce in DPoP proof.", cause)

class NotFoundException(message: String) : RuntimeException(message)
