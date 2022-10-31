package com.lecture.snsapp.exception

enum class ErrorCode(
  val errorCode: String,
  val message: String? = null
) {
  //common
  INTERNAL_SERVER_ERROR("90000", "Internal server error"),
  INVALID_PERMISSION("90001", "invalid permission"),

  //user
  INVALID_AUTHENTICATION("91000", "invalid authentication"),
  DUPLICATED_USER_NAME("91001", "user name is duplicated"),
  USER_NOT_FOUND("91002", "user not found"),
  LOGIN_FAILED("91003", "failed to login"),

  //post
  POST_NOT_FOUND("92001", "post not found"),

  //like
  ALREADY_LIKED_POST("93000", "already liked post")

}