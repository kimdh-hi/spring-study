package com.toy.oauth2clientcredentials.common.exception

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

  @ExceptionHandler(InvalidScopeException::class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  fun invalidScope(ex: InvalidScopeException) = ErrorResponse("invalid_scope", ex.message)

  @ExceptionHandler(InvalidClientMetadataException::class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  fun invalidClientMetadata(ex: InvalidClientMetadataException) = ErrorResponse("invalid_client_metadata", ex.message)

  @ExceptionHandler(InvalidCredentialsException::class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  fun invalidCredentials(ex: InvalidCredentialsException) = ErrorResponse("invalid_credentials", ex.message)

  @ExceptionHandler(NotFoundException::class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  fun notFound(ex: NotFoundException) = ErrorResponse("not_found", ex.message)
}

data class ErrorResponse(
  val error: String,
  @JsonProperty("error_description") val errorDescription: String?,
)
