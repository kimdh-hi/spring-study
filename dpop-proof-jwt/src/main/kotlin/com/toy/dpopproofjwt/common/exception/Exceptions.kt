package com.toy.dpopproofjwt.common.exception

class InvalidCredentialsException : RuntimeException("Bad credentials.")

class InvalidDPoPProofException(message: String?, cause: Throwable? = null) : RuntimeException(message, cause)

class NotFoundException(message: String) : RuntimeException(message)
