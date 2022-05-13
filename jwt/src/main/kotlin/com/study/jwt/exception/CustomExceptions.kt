package com.study.jwt.exception


abstract class BaseException(message: String, errorCode: String? = ErrorCodes.UNDEFINED, cause: Throwable? = null): RuntimeException(message, cause) {
    companion object {
        private const val serialVersionUID: Long = 7313601900731523692L
    }
}

class JwtAuthenticationException(message: String, errorCode: String): BaseException(message, errorCode) {
    companion object {
        private const val serialVersionUID: Long = -2265155784908155156L
    }
}

object ErrorCodes {
    const val RESOURCE_NOT_FOUND = "9001"
    const val INVALID_TOKEN = "9002"
    const val UNDEFINED = "9999"
}

