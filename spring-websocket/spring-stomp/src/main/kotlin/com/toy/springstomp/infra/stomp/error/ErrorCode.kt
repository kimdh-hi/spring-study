package com.toy.springstomp.infra.stomp.error

enum class ErrorCode(
  val message: String,
) {
  NOT_A_ROOM_USER("not a room user"),
  UNKNOWN("internal error"),
}
