package com.example.swaggerrestdocs.ui

import com.example.swaggerrestdocs.application.DuplicateEmailException
import com.example.swaggerrestdocs.application.UserNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiExceptionHandler {
    @ExceptionHandler(UserNotFoundException::class)
    fun notFound(exception: UserNotFoundException) =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse(exception.message ?: "Not found"))

    @ExceptionHandler(DuplicateEmailException::class)
    fun conflict(exception: DuplicateEmailException) =
        ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse(exception.message ?: "Conflict"))

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun validation(exception: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val message = exception.bindingResult.fieldErrors.firstOrNull()?.defaultMessage ?: "Invalid request"
        return ResponseEntity.badRequest().body(ErrorResponse(message))
    }
}

data class ErrorResponse(val message: String)
