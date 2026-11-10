package com.toy.dpopproofjwt.common.exception

class InvalidCredentialsException : RuntimeException("Bad credentials.")

class InvalidRefreshTokenException(message: String = "Refresh token is invalid or expired.") : RuntimeException(message)

class InvalidDPoPProofException(message: String?, cause: Throwable? = null) : RuntimeException(message, cause)

class UseDPoPNonceException(cause: Throwable? = null) :
  RuntimeException("Authorization server requires nonce in DPoP proof.", cause)

class NotFoundException(message: String) : RuntimeException(message)
