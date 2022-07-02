package com.example.ex.exception

import org.springframework.validation.BindingResult

abstract class BaseException: RuntimeException {

    constructor(): super()
    constructor(message: String): super(message)
    constructor(message: String, cause: Throwable): super(message, cause)

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