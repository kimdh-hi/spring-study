package com.study.openfeigncircuitbreaker.exception

open class OpenFeignException(
  val errorCode: Int,
  val httpStatusCode: Int,
  message: String? = null,
) : RuntimeException(message)
