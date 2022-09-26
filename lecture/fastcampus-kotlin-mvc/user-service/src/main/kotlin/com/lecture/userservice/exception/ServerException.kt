package com.lecture.userservice.exception

sealed class ServerException(
  open val code: Int,
  override val message: String
): RuntimeException(message)

data class AlreadyExistsDataException(
  override val message: String = "already exists data..."
): ServerException(9001, message)

data class NotFoundException(
  override val message: String = "data not found..."
): ServerException(9002, message)

data class UsernameOrPasswordNotMatchedException(
  override val message: String = "username or password not matched..."
): ServerException(9003, message)

data class InvalidJwtTokenException(
  override val message: String = "jwt token is invalid..."
): ServerException(9004, message)

