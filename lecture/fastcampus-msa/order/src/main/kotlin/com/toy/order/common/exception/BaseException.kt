package com.toy.order.common.exception

class BaseException(
  val errorCode: String,
  override val message: String? = null,
  override val cause: Throwable? = null
): RuntimeException(message, cause)