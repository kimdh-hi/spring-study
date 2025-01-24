package com.toy.springexception.exception

enum class ErrorCodes(val code: String) {
  DATA_NOT_FOUND("0001"),
  API_NOT_FOUND("0002"),
  UNKNOWN("9999"),
}
