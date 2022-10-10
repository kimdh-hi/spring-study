package com.lecture.snsapp.exception

enum class ErrorCode(
  val errorCode: String,
  val message: String? = null
) {
  INTERNAL_SERVER_ERROR("90000", "Internal server error"),

  INVALID_AUTHENTICATION("91000", "invalid authentication"),
  DUPLICATED_USER_NAME("91001", "user name is duplicated"),
  USER_NOT_FOUND("91002", "user not found"),
  LOGIN_FAILED("92003", "failed to login");

}