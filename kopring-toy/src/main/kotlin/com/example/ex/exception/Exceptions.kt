package com.example.ex.exception

import org.springframework.validation.BindingResult
import java.io.Serial

open class BaseException(
    private val errorCode: String? = ErrorCodes.UNKNOWN,
    message: String? = null,
    cause: Throwable? = null
) : RuntimeException(message, cause) {

    fun getErrorCode() = errorCode!!

    companion object {
        @Serial
        private const val serialVersionUID: Long = 3416992995011541346L
    }
}

class ParameterException(bindingResult: BindingResult): BaseException() {
    var errors: MutableMap<String, MutableList<String>> = mutableMapOf()

    init {
        bindingResult.fieldErrors.forEach {
            it.defaultMessage?.let { message ->
                errors.getOrPut(it.field, ::mutableListOf).add(message) }
        }
    }
}