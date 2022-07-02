package com.example.ex.controller.api

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.ExceptionHandler

abstract class ApiExceptionHandler {

    val log: Logger = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalStateException(ex: IllegalStateException): String {
        log.info(ex.message, ex)
        return ex.message as String
    }
}