package com.toy.springstomp.infra.stomp.error

class StompException(
  val errorCode: ErrorCode,
) : RuntimeException(errorCode.message)
