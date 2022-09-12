package com.toy.order.common.exception

class BaseException(
  val errorCode: String,
  message: String? = null
): RuntimeException(message)