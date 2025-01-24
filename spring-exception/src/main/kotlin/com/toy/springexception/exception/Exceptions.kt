package com.toy.springexception.exception

open class KnownException(val errorCodes: ErrorCodes, message: String? = null, cause: Throwable? = null) :
  RuntimeException(message, cause)
