package com.example.ex.exception

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

//@ControllerAdvice
class ControllerAdvice {

    val log: Logger = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): String {
        log.info(ex.message, ex)
        return "errors/5xx"
    }

    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalStateException(ex: IllegalStateException): String {
        log.info(ex.message, ex)
        return "errors/5xx"
    }
}