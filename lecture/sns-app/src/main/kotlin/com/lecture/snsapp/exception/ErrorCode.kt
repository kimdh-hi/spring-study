package com.lecture.snsapp.exception

enum class ErrorCode(
  val errorCode: String,
  val message: String? = null
) {
  INTERNAL_SERVER_ERROR("90000", "Internal server error"),

  DUPLICATED_USER_NAME("91001", "user name is duplicated"),
  LOGIN_FAILED("92002", "failed to login");

}