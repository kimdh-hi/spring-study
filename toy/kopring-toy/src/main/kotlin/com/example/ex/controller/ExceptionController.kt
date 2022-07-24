package com.example.ex.controller

import com.example.ex.dto.request.TestDto
import com.example.ex.exception.BaseException
import com.example.ex.exception.ErrorCodes
import com.example.ex.exception.ParameterException
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/exceptions")
class ExceptionController {

    @GetMapping("/base")
    fun baseException(): ResponseEntity<Unit> {
        throw BaseException(ErrorCodes.DATA_NOT_FOUND)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/validation/test")
    fun test(@RequestBody @Valid testDto: TestDto, bindingResult: BindingResult) : TestDto{
        if (bindingResult.hasErrors()) throw ParameterException(bindingResult)
        return testDto
    }

    @GetMapping("/exception")
    fun exception() {
        throw Exception("exception")
    }

    @ExceptionHandler(Exception::class)
    fun exceptionHandler(ex: Exception) = ex.message
}