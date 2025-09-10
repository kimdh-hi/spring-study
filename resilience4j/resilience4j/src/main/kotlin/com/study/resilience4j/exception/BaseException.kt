package com.study.resilience4j.exception

open class BaseException(
  val errorCode: String,
  message: String? = null,
  cause: Throwable? = null
) : RuntimeException(message, cause)
