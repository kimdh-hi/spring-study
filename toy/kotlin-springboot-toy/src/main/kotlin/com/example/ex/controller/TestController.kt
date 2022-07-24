package com.example.ex.controller

import com.example.ex.dto.request.TestDto
import com.example.ex.exception.ParameterException
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class TestController {

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