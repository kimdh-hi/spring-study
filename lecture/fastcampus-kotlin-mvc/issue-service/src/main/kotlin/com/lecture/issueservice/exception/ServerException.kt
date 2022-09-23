package com.lecture.issueservice.exception

sealed class ServerException(
  val code: Int,
  override val message: String
): RuntimeException(message)

data class NotFoundException(
  override val message: String
): ServerException(404, message)

data class UnauthorizedException(
  override val message: String = "인증 정보가 잘못됐습니"
): ServerException(401, message)