package com.toy.dpopproofjwt.common.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.MissingRequestHeaderException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

  @ExceptionHandler(InvalidDPoPProofException::class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  fun invalidDPoPProof(ex: InvalidDPoPProofException) = ErrorResponse("invalid_dpop_proof", ex.message)

  @ExceptionHandler(UseDPoPNonceException::class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  fun useDPoPNonce(ex: UseDPoPNonceException) = ErrorResponse("use_dpop_nonce", ex.message)

  @ExceptionHandler(MissingRequestHeaderException::class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  fun missingHeader(ex: MissingRequestHeaderException) =
    if (ex.headerName == "DPoP") ErrorResponse("invalid_dpop_proof", "DPoP proof is missing.")
    else ErrorResponse("invalid_request", ex.message)

  @ExceptionHandler(InvalidCredentialsException::class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  fun invalidCredentials(ex: InvalidCredentialsException) = ErrorResponse("invalid_grant", ex.message)

  @ExceptionHandler(NotFoundException::class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  fun notFound(ex: NotFoundException) = ErrorResponse("not_found", ex.message)
}

data class ErrorResponse(
  val error: String,
  val errorDescription: String?,
)
