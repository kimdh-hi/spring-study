package com.toy.oauth2clientcredentials.common.exception

class NotFoundException(message: String) : RuntimeException(message)

class InvalidScopeException(message: String) : RuntimeException(message)

class InvalidClientMetadataException(message: String) : RuntimeException(message)

class InvalidCredentialsException(message: String) : RuntimeException(message)
