package com.toy.webfluxr2dbcpostgres.common

import io.swagger.v3.oas.annotations.responses.ApiResponse

object ErrorCodes {

  const val UNKNOWN = "9999"
  const val DATA_NOT_FOUND = "9001"
  const val PARAMETER_NOT_VALID = "9002"
  const val EMAIL_SEND_FAIL = "9003"
  const val UNAUTHORIZED_ACCESS = "9004"
  const val UPLOAD_FAIL = "9005"
  const val NOT_SUPPORTED_LANGUAGE = "9006"

  const val DUPLICATE_COMPANY_NAME = "9100"

  const val USERNAME_OR_PASSWORD_NOT_MATCHED = "9200"
  const val USER_ACCOUNT_DISABLED = "9201"
  const val USER_ACCOUNT_LOCKED = "9202"
  const val DUPLICATE_USERNAME = "9203"
  const val NEED_EMAIL_AUTHENTICATION = "9204"
  const val ALREADY_AUTHENTICATED = "9205"
  const val PASSWORD_UPDATE_DATE_NOT_EXPIRED = "9206"
}