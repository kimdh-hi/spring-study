package com.lecture.userservice.exception

sealed class ServerException(
  open val code: Int,
  override val message: String
): RuntimeException(message)

data class AlreadyExistsDataException(
  override val message: String = "already exists data..."
): ServerException(9001, message)


